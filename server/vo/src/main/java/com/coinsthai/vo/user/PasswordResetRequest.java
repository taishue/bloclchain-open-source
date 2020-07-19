package com.coinsthai.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "重置密码请求")
public class PasswordResetRequest extends PasswordResetEmailRequest {

    private String verifyCode;

    private String password;
    
    @ApiModelProperty("验证码")
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @ApiModelProperty("新密码")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
