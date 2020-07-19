package com.coinsthai.module.oss;

import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Attachment;
import com.coinsthai.repository.AttachmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author
 */
@Service
public class PrivateFileService implements CloudFileService {

    @Autowired
    private OssService ossService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AliyunOssConfig ossConfig;

    @Override
    public Attachment save(AttachmentRequest request) {
        if (request == null || request.getInputStream() == null || request.getUser() == null ||
                StringUtils.isAnyBlank(request.getContentKey(), request.getContentType())
                || request.getContentKey().length() > 255) {
            throw new BizException(BizErrorCode.ATTACHMENT_ILLEGAL_ARGUMENTS);
        }

        Attachment attachment = new Attachment();
        attachment.setUser(request.getUser());
        attachment.setBucket(getBucket());
        attachment.setContentKey(request.getContentKey());
        attachment.setContentLength(request.getContentLength());
        attachment.setContentType(request.getContentType());
        Attachment attachmentSaved = attachmentRepository.save(attachment);

        ossService.upload(getBucket(), request.getContentKey(), request.getInputStream());

        return attachmentSaved;
    }

    @Override
    public InputStream get(String key) {
        return ossService.download(getBucket(), key);
    }

    private String getBucket() {
        return ossConfig.getPrivateBucket();
    }
}
