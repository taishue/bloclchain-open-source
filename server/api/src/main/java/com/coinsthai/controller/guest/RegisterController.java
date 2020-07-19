package com.coinsthai.controller.guest;

import com.coinsthai.model.User;
import com.coinsthai.security.SecurityUserConverter;
import com.coinsthai.service.RegisterService;
import com.coinsthai.vo.user.RegisterRequest;
import com.coinsthai.vo.user.RegisterResult;
import com.coinsthai.vo.TraditionalResult;
import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.federation.FederationService;
import in.clouthink.daas.security.token.spi.impl.SimpleFederationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */
@Api(description = "注册API")
@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private FederationService federationService;

    @Autowired
    private SecurityUserConverter securityUserConverter;

    @ApiOperation(value = "通过Email注册")
    @PostMapping("/email")
    public RegisterResult register(@RequestBody RegisterRequest request) {
        request.setLocale(LocaleContextHolder.getLocale().toString());
        return registerService.register(request);
    }

    @ApiOperation(value = "重发激活邮件")
    @PostMapping("/email/{id}/resend")
    public void resendActiveMail(@ApiParam("注册请求ID") @PathVariable String id) {
        registerService.resendActiveEmail(id);
    }

    @ApiOperation(value = "激活注册帐号")
    @GetMapping("/activations/{id}")
    public TraditionalResult<String> active(@ApiParam("注册请求ID") @PathVariable String id,
                                            @ApiParam("激活码") @RequestParam String activeCode) {
        User user = registerService.activeUser(id, activeCode);
        SimpleFederationRequest federationRequest = new SimpleFederationRequest(securityUserConverter.toPojo(user));
        Authentication authentication = federationService.login(federationRequest);

        TraditionalResult<String> result = new TraditionalResult<>();
        result.setSucceed(true);
        result.setData(authentication.currentToken().getToken());

        return result;
    }
}
