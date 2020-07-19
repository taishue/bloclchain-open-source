package com.coinsthai.controller.guest;

import com.coinsthai.service.UserService;
import com.coinsthai.vo.user.PasswordResetEmailRequest;
import com.coinsthai.vo.user.PasswordResetRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 */
@Api(description = "找回密码API")
@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "请求找回密码")
    @PostMapping("/email")
    public void request(@RequestBody PasswordResetEmailRequest request) {
        userService.requestPasswordReset(request);
    }

    @ApiOperation(value = "重置密码")
    @PostMapping
    public void resetPassword(@RequestBody PasswordResetRequest request) {
        userService.resetPassword(request);
    }

}
