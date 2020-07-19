package com.coinsthai.module.kyc;

import com.coinsthai.cache.FaceIdTokenResponseCache;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Attachment;
import com.coinsthai.model.IdCard;
import com.coinsthai.model.User;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.kyc.rest.*;
import com.coinsthai.module.oss.AttachmentRequest;
import com.coinsthai.module.oss.CloudFileService;
import com.coinsthai.pojo.intenum.Gender;
import com.coinsthai.repository.IdCardRepository;
import com.coinsthai.service.UserService;
import com.coinsthai.util.CommonUtils;
import com.coinsthai.util.DateUtils;
import com.coinsthai.util.JsonUtils;
import com.coinsthai.vo.kyc.FaceIdToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Component
public class FaceIdServiceImpl implements FaceIdService {

    @Autowired
    private FaceIdTokenResponseCache faceIdTokenResponseCache;

    @Autowired
    private FaceIdConfiguration faceIdConfiguration;

    @Autowired
    private UserService userService;

    @Autowired
    private IdCardRepository idCardRepository;

    @Autowired
    private CloudFileService cloudFileService;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PATTERN_BIRTHDAY = "yyyyMMdd";

    private static final String PATTERN_VALID_DATE = "yyyy.MM.dd";

    @Override
    public FaceIdToken requestToken(FaceIdTokenRequest request) {
        if (StringUtils.isAllBlank(request.getId(), request.getUserId())) {
            BizException ex = new BizException(BizErrorCode.FACEID_ID_EMPTY);
            auditLogger.fail("get", "faceid token", "", ex);
        }

        FaceIdTokenResponse response = null;
        if (StringUtils.isNotBlank(request.getId())) {
            response = faceIdTokenResponseCache.get(request.getId());
            if (response == null) {
                BizException ex = new BizException(BizErrorCode.FACEID_ID_EMPTY);
                auditLogger.fail("get", "faceid token", "", ex);
            }
        }
        else {
            response = new FaceIdTokenResponse();
            response.setUserId(request.getUserId());
            request.setId(CommonUtils.simpleUUID());
        }
        checkAlreadyVerified(request.getUserId());

        FaceIdTokenRaw rawToken = getToken(request.getId());
        response.setBizId(rawToken.getBizId());
        response.setRequestId(rawToken.getRequestId());
        response.setToken(rawToken.getToken());
        faceIdTokenResponseCache.set(request.getId(), response, 40, TimeUnit.MINUTES);

        FaceIdToken token = new FaceIdToken();
        token.setToken(response.getToken());
        token.setId(request.getId());
        return token;
    }

    @Transactional
    @Override
    public boolean handleNotify(FaceIdNotifyResultRaw notifyResult) {
        if (StringUtils.isAnyBlank(notifyResult.getData(), notifyResult.getSign())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        // 检验签名
        String localSign = DigestUtils.sha1Hex(faceIdConfiguration.getAppSecret() + notifyResult.getData());
        if (!localSign.equalsIgnoreCase(notifyResult.getSign())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        FaceIdResultRaw resultRaw = JsonUtils.parseJson(notifyResult.getData(), FaceIdResultRaw.class);
        if (!"OK".equals(resultRaw.getStatus())) {
            return false;
        }
        FaceIdTokenResponse tokenResponse = faceIdTokenResponseCache.get(resultRaw.getBizInfo().getBizNo());
        if (tokenResponse == null) {
            return false;
        }

        // 解析出身份证信息
        IdCard idCard = resolveIdCardBasic(resultRaw, tokenResponse);
        if (idCard == null) {
            return false;
        }

        User user = userService.get(tokenResponse.getUserId());
        idCard.setUser(user);
        idCardRepository.save(idCard);

        // 更新用户上的标记
        user.setIdVerify(true);
        user.setBioVerify(true);
        userService.update(user);

        // 通知去下载图片
        IdCardVerifiedEvent event = new IdCardVerifiedEvent(resultRaw.getBizInfo().getBizNo());
        eventPublisher.publish(event);

        return true;
    }

    @Override
    public boolean verify(String id, FaceIdNotifyResultRaw resultRaw) {
        FaceIdTokenResponse tokenResponse = faceIdTokenResponseCache.get(id);
        if (tokenResponse == null) {
            return false;
        }

        User user = userService.get(tokenResponse.getUserId());
        if (user.isBioVerify()) {
            return true;
        }

        return handleNotify(resultRaw);
    }

    @Transactional
    @Override
    public void requestResult(String id) {
        FaceIdTokenResponse tokenResponse = faceIdTokenResponseCache.get(id);
        if (tokenResponse == null) {
            throw new BizException(BizErrorCode.FACEID_ID_EMPTY, "id", id);
        }

        IdCard idCard = idCardRepository.findByUserId(tokenResponse.getUserId());
        if (idCard != null && !StringUtils.isAllBlank(idCard.getFrontImageKey(),
                                                      idCard.getBackImageKey(),
                                                      idCard.getHoldImageKey())) {
            return; // 已保存
        }

        FaceIdResultRaw resultRaw = getResult(tokenResponse.getBizId());
        idCard = resolveIdCardBasic(resultRaw, tokenResponse);

        // 获取相关照片
        Images images = resultRaw.getImages();

        Attachment frontAttachment = saveImage(images.getIdCardFront(), "idcard_front", idCard.getUser());
        if (frontAttachment != null) {
            idCard.setFrontImageKey(frontAttachment.getContentKey());
        }

        Attachment backAttachment = saveImage(images.getIdCardBack(), "idcard_back", idCard.getUser());
        if (backAttachment != null) {
            idCard.setBackImageKey(backAttachment.getContentKey());
        }

        Attachment bestAttachment = saveImage(images.getBest(), "face", idCard.getUser());
        if (bestAttachment != null) {
            idCard.setHoldImageKey(bestAttachment.getContentKey());
        }

        idCardRepository.save(idCard);
    }

    private Attachment saveImage(String rawString, String imageName, User user) {
        if (StringUtils.isBlank(rawString)) {
            return null;
        }

        AttachmentRequest attachmentRequest = new AttachmentRequest();
        attachmentRequest.setUser(user);
        convertToAttachment(rawString, attachmentRequest);

        String key = user.getId() + "/kyc/" + CommonUtils.simpleUUID() + "-" + imageName +
                getImageSuffix(attachmentRequest.getContentType());
        attachmentRequest.setContentKey(key);
        return cloudFileService.save(attachmentRequest);
    }

    private void convertToAttachment(String rawString, AttachmentRequest attachment) {
        String[] sections1 = StringUtils.split(rawString, ':');
        if (sections1.length < 2) {
            throw new BizException(BizErrorCode.FACEID_ILLEGAL_IMAGE);
        }

        String[] sections2 = StringUtils.split(sections1[1], ';');
        String contentType = StringUtils.trim(sections2[0]);
        attachment.setContentType(contentType);

        String base64Data = StringUtils.trim(sections2[1].substring(7));
        byte[] bytes = Base64.decodeBase64(base64Data);
        attachment.setContentLength(bytes.length);
        attachment.setInputStream(new ByteArrayInputStream(bytes));
    }

    private String getImageSuffix(String contentType) {
        if ("image/jpeg".equalsIgnoreCase(contentType) || "image/jpg".equalsIgnoreCase(contentType) ||
                "image/pjpeg".equalsIgnoreCase(contentType)) {
            return ".jpg";
        }
        if ("image/x-png".equalsIgnoreCase(contentType) || "image/png".equalsIgnoreCase(contentType)) {
            return ".png";
        }
        return ".jpg";
    }

    private boolean isVerified(Confidence confidence) {
        double standard = confidence.getThresholds().get(faceIdConfiguration.getConfidenceThreshold());
        if (confidence.getConfidence() < standard) {
            return false;
        }
        return true;
    }

    private void checkAlreadyVerified(String userId) {
        User user = userService.get(userId);
        if (user.isBioVerify()) {
            BizException ex = new BizException(BizErrorCode.FACEID_ALREADY_VERIFIED);
            auditLogger.fail("get", "faceid token", "", ex);
        }
    }

    private IdCard resolveIdCardBasic(FaceIdResultRaw resultRaw, FaceIdTokenResponse tokenResponse) {
        VerifyResult verifyResult = resultRaw.getVerifyResult();
        if (verifyResult == null ||
                (verifyResult.getResultFaceid() == null && verifyResult.getResultIdcardDatasource() == null &&
                        verifyResult.getResultIdcardPhoto() == null)) {
            return null;
        }

        if (verifyResult.getResultFaceid() != null && !isVerified(verifyResult.getResultFaceid())) {
            return null;
        }
        if (verifyResult.getResultIdcardDatasource() != null && !isVerified(verifyResult.getResultIdcardDatasource())) {
            return null;
        }
        if (verifyResult.getResultIdcardPhoto() != null && !isVerified(verifyResult.getResultIdcardPhoto())) {
            return null;
        }

        CardInfo cardInfo = resultRaw.getCardInfo();
        if (cardInfo == null || cardInfo.getBackSide() == null || cardInfo.getBackSide().getOcrResult() == null ||
                cardInfo.getFrontSide() == null || cardInfo.getFrontSide().getOcrResult() == null) {
            return null;
        }

        // 保存身份证信息
        CardFrontSide frontSide = cardInfo.getFrontSide().getOcrResult();
        CardBackSide backSide = cardInfo.getBackSide().getOcrResult();
        IdCard idCard = idCardRepository.findByUserId(tokenResponse.getUserId());
        if (idCard == null) {
            idCard = new IdCard();
        }
        idCard.setName(frontSide.getName());
        idCard.setCardNumber(frontSide.getIdCardNumber());
        idCard.setAddress(frontSide.getAddress());
        idCard.setBirthday(convertBirthday(frontSide.getBirthday()));
        idCard.setGender(Gender.parse(frontSide.getGender()));
        idCard.setRace(frontSide.getRace());
        idCard.setIssuedBy(backSide.getIssuedBy());
        resolveValidDate(idCard, backSide.getValidDate());

        return idCard;
    }

    private Date convertBirthday(Birthday birthday) {
        if (birthday == null) {
            return null;
        }

        StringBuilder dateStr = new StringBuilder(birthday.getYear());

        if (birthday.getMonth().length() < 2) {
            dateStr.append("0");
        }
        dateStr.append(birthday.getMonth());

        if (birthday.getDay().length() < 2) {
            dateStr.append("0");
        }
        dateStr.append(birthday.getDay());

        return DateUtils.parseDateSilent(dateStr.toString(), PATTERN_BIRTHDAY);
    }

    private void resolveValidDate(IdCard idCard, String validDateStr) {
        if (StringUtils.isBlank(validDateStr)) {
            return;
        }
        String[] sections = StringUtils.split(validDateStr, '-');
        if (sections == null || sections.length < 2) {
            return;
        }

        idCard.setValidBeginAt(DateUtils.parseDateSilent(sections[0], PATTERN_VALID_DATE));
        idCard.setValidEndAt(DateUtils.parseDateSilent(sections[1], PATTERN_VALID_DATE));
    }

    private FaceIdResultRaw getResult(String bizId) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(faceIdConfiguration.getResultUrl())
                                                           .queryParam("api_key", faceIdConfiguration.getAppKey())
                                                           .queryParam("api_secret", faceIdConfiguration.getAppSecret())
                                                           .queryParam("biz_id", bizId)
                                                           .queryParam("return_image",
                                                                       faceIdConfiguration.getReturnImage());

        try {
            return restTemplate.getForObject(builder.build().toUri(), FaceIdResultRaw.class);
        } catch (RestClientException e) {
            BizException ex = new BizException(BizErrorCode.FACEID_REMOTE_ERROR, e);
            auditLogger.fail("get", "faceid result", bizId, ex);
            return null;
        }
    }

    private FaceIdTokenRaw getToken(String id) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("api_key", faceIdConfiguration.getAppKey());
        parameters.add("api_secret", faceIdConfiguration.getAppSecret());
        parameters.add("return_url", String.format(faceIdConfiguration.getReturnUrl(), id));
        parameters.add("notify_url", faceIdConfiguration.getNotifyUrl());
        parameters.add("biz_no", id);
        parameters.add("web_title", faceIdConfiguration.getWebTitle());
        parameters.add("scene_id", faceIdConfiguration.getScene());
        parameters.add("procedure_type", faceIdConfiguration.getProcedureType());
        parameters.add("liveness_preferences", faceIdConfiguration.getLivenessPreferences());
        parameters.add("comparison_type", faceIdConfiguration.getComparisonType());
        parameters.add("idcard_mode", faceIdConfiguration.getIdcardMode());
        parameters.add("idcard_uneditable_field", faceIdConfiguration.getIdcardUneditableFields());
        parameters.add("multi_oriented_detection", faceIdConfiguration.getMultiOrientedDetection());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity entity = new HttpEntity(parameters, headers);
            ResponseEntity<FaceIdTokenRaw> responseEntity = restTemplate.exchange(faceIdConfiguration.getTokenUrl(),
                                                                                  HttpMethod.POST,
                                                                                  entity,
                                                                                  FaceIdTokenRaw.class);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            BizException ex = new BizException(BizErrorCode.FACEID_REMOTE_ERROR, e);
            auditLogger.fail("get", "faceid token", id, ex);
            return null;
        }
    }
}
