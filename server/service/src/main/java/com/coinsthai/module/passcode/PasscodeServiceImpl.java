package com.coinsthai.module.passcode;

import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.cache.PasscodeCache;
import com.coinsthai.config.MessageResolver;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.edm.event.PasscodeEvent;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.User;
import com.coinsthai.module.email.EmailService;
import com.coinsthai.module.passcode.PasscodeService;
import com.coinsthai.service.UserService;
import com.coinsthai.util.CommonUtils;
import com.coinsthai.module.email.EmailContent;
import com.coinsthai.module.passcode.PasscodeContent;
import com.coinsthai.module.passcode.PasscodeRequest;
import com.coinsthai.vo.PasscodeResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Service
public class PasscodeServiceImpl implements PasscodeService {

    @Autowired
    private PasscodeCache passcodeCache;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private MessageResolver messageResolver;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    public PasscodeResponse request(PasscodeRequest request) {
        PasscodeContent content = null;
        if (StringUtils.isNotBlank(request.getId())) {
            content = passcodeCache.get(request.getId());
        }
        if (content != null && (!content.getOperation().equals(request.getOperation()) ||
                !content.getReceiverId().equals(request.getReceiverId()))) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        if (content == null) {
            content = new PasscodeContent();
            content.setCode(RandomStringUtils.randomNumeric(6));
            content.setOperation(request.getOperation());
            content.setReceiverId(request.getReceiverId());
            content.setId(CommonUtils.simpleUUID());
            passcodeCache.set(content.getId(), content, 30, TimeUnit.MINUTES);
        }

        // 发送事件通知
        PasscodeEvent event = new PasscodeEvent(content.getId());
        eventPublisher.publish(event);

        PasscodeResponse response = new PasscodeResponse();
        response.setId(content.getId());
        return response;
    }

    @Override
    public void sendEmail(String id) {
        PasscodeContent content = passcodeCache.get(id);
        if (content == null) {
            auditLogger.fail("send", "passcode", id, null);
            return;
        }

        sendEmail(content);
    }

    @Override
    public boolean verify(String id, String code, boolean expire) {
        PasscodeContent content = passcodeCache.get(id);
        if (content == null || !content.getCode().equals(code)) {
            return false;
        }

        if (expire) {
            passcodeCache.delete(id);
        }

        return true;
    }

    private void sendEmail(PasscodeContent request) {
        User receiver = userService.get(request.getReceiverId());
        String operation = messageResolver.getMessage(request.getOperation(), receiver.getLocale());
        Map<String, String> context = new HashMap<>();
        context.put("name", receiver.getName());
        context.put("operation", operation);
        context.put("code", request.getCode());

        EmailContent content = new EmailContent();
        content.setSubject(messageResolver.getMessage("EMAIL_PASSCODE_SUBJECT", receiver.getLocale()));
        content.setTemplate(messageResolver.getMessage("EMAIL_PASSCODE_TEMPLATE", receiver.getLocale()));
        content.setTo(receiver.getEmail());
        content.setContext(context);

        emailService.send(content);
    }
}
