package com.coinsthai.vo.user;

import com.coinsthai.pojo.common.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "找回密码验证邮箱请求")
public class PasswordResetEmailRequest extends BasePojo {

    @ApiModelProperty("注册邮箱")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
