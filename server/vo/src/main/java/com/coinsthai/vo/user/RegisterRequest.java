package com.coinsthai.vo.user;

import com.coinsthai.pojo.common.BasePojo;
import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "注册请求")
public class RegisterRequest extends StringIdentifier {

    private String email;

    private String password;

    private String locale;

    // 激活码
    private String activeCode;

    @ApiModelProperty(value = "邮箱")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ApiModelProperty("密码")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ApiModelProperty(hidden = true)
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty(hidden = true)
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @ApiModelProperty(hidden = true)
    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
}
