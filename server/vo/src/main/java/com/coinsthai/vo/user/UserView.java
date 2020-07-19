package com.coinsthai.vo.user;

import com.coinsthai.pojo.UserPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author 
 */
@ApiModel(description = "用户")
public class UserView extends UserPojo {

    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty("邮箱")
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @ApiModelProperty("是否激活")
    @Override
    public boolean isActive() {
        return super.isActive();
    }

    @ApiModelProperty("是否通过了身份认证")
    @Override
    public boolean isIdVerify() {
        return super.isIdVerify();
    }

    @ApiModelProperty("是否通过了生物认证")
    @Override
    public boolean isBioVerify() {
        return super.isBioVerify();
    }

    @ApiModelProperty("是否通过了银行卡认证")
    @Override
    public boolean isBankVerify() {
        return super.isBankVerify();
    }

    @ApiModelProperty("语言")
    @Override
    public String getLocale() {
        return super.getLocale();
    }

    @ApiModelProperty("手机号")
    @Override
    public String getCellphone() {
        return super.getCellphone();
    }

    @ApiModelProperty("上次登录时间")
    @Override
    public Date getLastLoggedAt() {
        return super.getLastLoggedAt();
    }

    @ApiModelProperty("上次登录IP")
    @Override
    public String getLastLoggedIp() {
        return super.getLastLoggedIp();
    }

    @ApiModelProperty(hidden = true)
    @Override
    public String getName() {
        return super.getName();
    }

    @ApiModelProperty(hidden = true)
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @ApiModelProperty(hidden = true)
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }
}
