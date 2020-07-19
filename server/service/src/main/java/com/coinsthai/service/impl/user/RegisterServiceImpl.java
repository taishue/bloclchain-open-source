package com.coinsthai.service.impl.user;

import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.cache.RegisterRequestCache;
import com.coinsthai.config.MessageResolver;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.edm.event.UserRegisteringEvent;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.User;
import com.coinsthai.repository.UserRepository;
import com.coinsthai.module.email.EmailService;
import com.coinsthai.service.RegisterService;
import com.coinsthai.service.UserService;
import com.coinsthai.util.CommonUtils;
import com.coinsthai.module.email.EmailContent;
import com.coinsthai.vo.user.RegisterRequest;
import com.coinsthai.vo.user.RegisterResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterRequestCache registerRequestCache;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    EmailService emailService;

    @Value("${app.email.register.link}")
    private String registerActiveLink;

    @Autowired
    private MessageResolver messageResolver;


    @Override
    public RegisterResult register(RegisterRequest request) {
        if (StringUtils.isBlank(request.getEmail()) || !emailValidator.isValid(request.getEmail())) {
            BizException ex = new BizException(BizErrorCode.USER_EMAIL_ILLEGAL, "email", request.getEmail());
            auditLogger.fail("register", AuditEntity.USER, request.getEmail(), ex);
        }
        if (StringUtils.isBlank(request.getPassword()) || !passwordValidator.isValid(request.getPassword())) {
            BizException ex = new BizException(BizErrorCode.USER_PASSWORD_ILLEGAL, "password", request.getPassword());
            auditLogger.fail("register", AuditEntity.USER, request.getEmail(), ex);
        }

        if (userRepository.countByEmail(request.getEmail()) > 0) {
            BizException ex = new BizException(BizErrorCode.USER_EMAIL_EXISTS, "email", request.getEmail());
            auditLogger.fail("register", AuditEntity.USER, request.getEmail(), ex);
        }

        RegisterRequest existsRequest = registerRequestCache.get(request.getEmail());
        if (existsRequest != null) {
            existsRequest.setPassword(request.getPassword());
            registerRequestCache.set(existsRequest.getId(), existsRequest);
            registerRequestCache.set(request.getEmail(), existsRequest);
            return createRegisterResult(existsRequest.getId());
        }

        request.setActiveCode(RandomStringUtils.randomNumeric(6));
        String registerId = CommonUtils.simpleUUID();
        request.setId(registerId);
        registerRequestCache.set(registerId, request);
        registerRequestCache.set(request.getEmail(), request);

        //发送事件
        UserRegisteringEvent event = new UserRegisteringEvent(registerId);
        eventPublisher.publish(event);

        return createRegisterResult(registerId);
    }

    private RegisterResult createRegisterResult(String registerId) {
        RegisterResult result = new RegisterResult();
        result.setId(registerId);
        return result;
    }

    @Override
    public void resendActiveEmail(String registerId) {
        RegisterRequest request = registerRequestCache.get(registerId);
        if (request == null) {
            BizException ex = new BizException(BizErrorCode.REGISTER_TIMEOUT, "registerId", registerId);
            auditLogger.fail("resend", "active email", registerId, ex);
        }

        //发送事件
        UserRegisteringEvent event = new UserRegisteringEvent(registerId);
        eventPublisher.publish(event);
    }

    @Override
    public User activeUser(String registerId, String activeCode) {
        if (StringUtils.isAnyBlank(registerId, activeCode)) {
            BizException ex = new BizException(BizErrorCode.REGISTER_TIMEOUT,
                                               "registerId,activeCode",
                                               registerId,
                                               activeCode);
            auditLogger.fail("activate", AuditEntity.USER, registerId, ex);
        }

        RegisterRequest request = registerRequestCache.get(registerId);
        if (request == null) {
            BizException ex = new BizException(BizErrorCode.REGISTER_TIMEOUT, "registerId", registerId);
            auditLogger.fail("activate", AuditEntity.USER, registerId, ex);
        }
        if (!activeCode.equals(request.getActiveCode())) {
            BizException ex = new BizException(BizErrorCode.REGISTER_ACTIVATE_ILLEGAL,
                                               "registerId,activeCode",
                                               registerId,
                                               activeCode);
            auditLogger.fail("activate", AuditEntity.USER, registerId, ex);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setLocale(request.getLocale());
        userService.create(user);

        //删除缓存
        registerRequestCache.delete(registerId);
        registerRequestCache.delete(request.getEmail());

        return user;
    }

    @Override
    public void sendActiveEmail(String registerId) {
        RegisterRequest request = registerRequestCache.get(registerId);
        if (request == null) {
            auditLogger.fail("send", "active email", registerId, null);
            return;
        }

        EmailContent emailContent = new EmailContent();
        emailContent.setSubject(messageResolver.getMessage("EMAIL_REGISTER_SUBJECT", request.getLocale()));
        emailContent.setTemplate(messageResolver.getMessage("EMAIL_REGISTER_TEMPLATE", request.getLocale()));
        emailContent.setTo(request.getEmail());
        Map<String, Object> context = new HashMap<>();
        String activeLink = String.format(registerActiveLink, registerId, request.getActiveCode());
        context.put("activeLink", activeLink);
        context.put("id", registerId);
        context.put("activeCode", request.getActiveCode());
        context.put("name", request.getEmail().split("@")[0]);
        emailContent.setContext(context);
        emailService.send(emailContent);
    }

}
