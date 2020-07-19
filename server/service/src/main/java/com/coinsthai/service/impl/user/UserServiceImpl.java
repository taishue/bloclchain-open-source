package com.coinsthai.service.impl.user;

import com.coinsthai.model.IdCard;
import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.cache.PasswordResetCodeCache;
import com.coinsthai.config.MessageResolver;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.edm.event.PasswordResetRequestEvent;
import com.coinsthai.module.edm.event.UserCreatedEvent;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.User;
import com.coinsthai.repository.IdCardRepository;
import com.coinsthai.repository.UserRepository;
import com.coinsthai.module.email.EmailService;
import com.coinsthai.service.UserService;
import com.coinsthai.util.CommonUtils;
import com.coinsthai.module.email.EmailContent;
import com.coinsthai.vo.user.CellphoneUpdateRequest;
import com.coinsthai.vo.user.PasswordResetEmailRequest;
import com.coinsthai.vo.user.PasswordResetRequest;
import com.coinsthai.vo.user.PasswordUpdateRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdCardRepository idCardRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordResetCodeCache resetCodeCache;

    @Autowired
    private PasswordValidator passwordValidator;

    private Random ranGen = new SecureRandom();

    private PasswordEncoder passwordEncoder = PasswordEncoder.MD5;

    @Value("${app.email.passwordReset.link}")
    private String passwordResetLink;

    @Autowired
    private MessageResolver messageResolver;

    @Autowired
    EmailService emailService;

    @Override
    public User create(User user) {
        checkCreatingUser(user);
        userRepository.save(user);
        auditLogger.success(AuditAction.CREATED, AuditEntity.USER, user.getEmail());

        // 发送事件通知
        eventPublisher.publish(new UserCreatedEvent(user.getId()));

        return user;
    }

    @Override
    public User update(User user) {
        user = userRepository.save(user);
        auditLogger.success(AuditAction.UPDATED, "user", user.getId());

        return user;
    }

    @Override
    public User updateCellphone(CellphoneUpdateRequest request) {
        User user = userRepository.findOne(request.getId());
        if (user == null) {
            BizException ex = new BizException(BizErrorCode.USER_NOT_FOUND, "id", request.getId());
            auditLogger.fail(AuditAction.UPDATING, "cellphone", request.getId(), ex);
        }

        user.setCellphone(request.getCellphone());
        user = userRepository.save(user);
        auditLogger.success(AuditAction.UPDATING, "cellphone", request.getId());

        return user;
    }

    @Override
    public User updatePassword(PasswordUpdateRequest request) {
        if (StringUtils.isBlank(request.getOldPassword())) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_OLD_MISMATCH,
                                               "id,oldPassword",
                                               request.getId(),
                                               request.getOldPassword());
            auditLogger.fail(AuditAction.UPDATING, "password", request.getId(), ex);
        }
        if (StringUtils.isBlank(request.getPassword()) || !passwordValidator.isValid(request.getPassword())) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_NEW_ILLEGAL,
                                               "id,password",
                                               request.getId(),
                                               request.getPassword());
            auditLogger.fail(AuditAction.UPDATING, "password", request.getId(), ex);
        }

        User user = userRepository.findOne(request.getId());
        if (user == null) {
            BizException ex = new BizException(BizErrorCode.USER_NOT_FOUND, "id", request.getId());
            auditLogger.fail(AuditAction.UPDATING, "cellphone", request.getId(), ex);
        }

        String oldHash = generatePasswordHash(request.getOldPassword(), user.getPasswordSalt());
        if (!oldHash.equals(user.getPassword())) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_OLD_MISMATCH, "id", request.getId());
            auditLogger.fail(AuditAction.UPDATING, "password", request.getId(), ex);
        }

        String newHash = generatePasswordHash(request.getPassword(), user.getPasswordSalt());
        user.setPassword(newHash);
        user = userRepository.save(user);
        auditLogger.success(AuditAction.UPDATING, "password", request.getId());

        return user;
    }

    @Override
    public User get(String id) {
        return userRepository.findOne(id);
    }

    private void checkCreatingUser(User user) {
        if (StringUtils.isBlank(user.getEmail()) || !emailValidator.isValid(user.getEmail())) {
            BizException ex = new BizException(BizErrorCode.USER_EMAIL_ILLEGAL, "email", user.getEmail());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.USER, user.getEmail(), ex);
        }
        if (StringUtils.isBlank(user.getPassword()) || !passwordValidator.isValid(user.getPassword())) {
            BizException ex = new BizException(BizErrorCode.USER_PASSWORD_ILLEGAL, "password", user.getPassword());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.USER, "", ex);
        }

        if (StringUtils.isBlank(user.getEmail())) {
            BizException ex = new BizException(BizErrorCode.USER_EMAIL_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.USER, "", ex);
        }

        if (userRepository.countByEmail(user.getEmail()) > 0) {
            BizException ex = new BizException(BizErrorCode.USER_EMAIL_EXISTS, "email", user.getEmail());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.USER, user.getEmail(), ex);
        }

        if (StringUtils.isBlank(user.getPasswordSalt())) {
            user.setPasswordSalt(generatePasswordSalt());
        }
        user.setPassword(generatePasswordHash(user.getPassword(),
                                              user.getPasswordSalt()));
    }

    private String generatePasswordSalt() {
        byte[] aesKey = new byte[16];
        ranGen.nextBytes(aesKey);
        String salt = Base64.getEncoder().encodeToString(aesKey);
        salt = salt.replace("\r", "");
        salt = salt.replace("\n", "");
        return salt;
    }

    private String generatePasswordHash(String rawPassword, String salt) {
        String password = rawPassword;
        if (StringUtils.isBlank(rawPassword)) {
            password = CommonUtils.simpleUUID();
        }
        return passwordEncoder.encode(password, salt);
    }

    @Override
    public User getByLogin(String emailOrCellphone) {
        return userRepository.findByEmail(emailOrCellphone);
    }

    @Override
    public IdCard getByUser(String userId) {
        return idCardRepository.findByUserId(userId);
    }

    @Override
    public void requestPasswordReset(PasswordResetEmailRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_EMAIL_ILLEGAL, "email", email);
            auditLogger.fail("reset", "password", email, ex);
        }

        String verifyCode = resetCodeCache.get(email);
        if (StringUtils.isBlank(verifyCode)) {
            verifyCode = RandomStringUtils.randomNumeric(6);
        }

        resetCodeCache.set(email, verifyCode);

        // 发送事件通知
        PasswordResetRequestEvent event = new PasswordResetRequestEvent(email);
        eventPublisher.publish(event);
    }

    @Override
    public User resetPassword(PasswordResetRequest request) {
        String email = request.getEmail();
        String inputCode = request.getVerifyCode();
        if (StringUtils.isBlank(email)) {
            BizException ex = new BizException(BizErrorCode.USER_EMAIL_EMPTY, "email", email);
            auditLogger.fail("reset", "password", email, ex);
        }
        if (StringUtils.isBlank(inputCode)) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_CODE_ILLEGAL, "email", email);
            auditLogger.fail("reset", "password", email, ex);
        }

        String savedCode = resetCodeCache.get(email);
        if (StringUtils.isBlank(savedCode) || !inputCode.equals(savedCode)) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_CODE_ILLEGAL,
                                               "email,verifyCode",
                                               email,
                                               inputCode);
            auditLogger.fail("reset", "password", email, ex);
        }

        if (StringUtils.isBlank(request.getPassword()) || !passwordValidator.isValid(request.getPassword())) {
            BizException ex = new BizException(BizErrorCode.PASSWORD_NEW_ILLEGAL,
                                               "email,password",
                                               email,
                                               request.getPassword());
            auditLogger.fail("reset", "password", email, ex);
        }

        User user = userRepository.findByEmail(email);
        if (StringUtils.isBlank(user.getPasswordSalt())) {
            user.setPasswordSalt(generatePasswordSalt());
        }
        user.setPassword(generatePasswordHash(request.getPassword(),
                                              user.getPasswordSalt()));

        resetCodeCache.delete(email);   //移除验证码
        return userRepository.save(user);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        String code = resetCodeCache.get(email);
        if (StringUtils.isBlank(code)) {
            auditLogger.fail("send", "password reset email", email, null);
            return;
        }

        User user = userRepository.findByEmail(email);
        EmailContent emailContent = new EmailContent();
        emailContent.setSubject(messageResolver.getMessage("EMAIL_PASSWORD_SUBJECT", user.getLocale()));
        emailContent.setTemplate(messageResolver.getMessage("EMAIL_PASSWORD_TEMPLATE", user.getLocale()));
        emailContent.setTo(email);
        Map<String, Object> context = new HashMap<>();
        String emailOnUrl = StringUtils.replace(email, "@", "%40");
        String reset = String.format(passwordResetLink, emailOnUrl, code);
        context.put("resetLink", reset);
        context.put("verifyCode", code);
        context.put("name", email.split("@")[0]);
        emailContent.setContext(context);
        emailService.send(emailContent);
    }
}
