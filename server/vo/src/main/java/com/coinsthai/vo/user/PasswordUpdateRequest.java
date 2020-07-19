package com.coinsthai.vo.user;

import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "修改密码请求")
public class PasswordUpdateRequest extends StringIdentifier {

    private String password;

    private String oldPassword;

    @ApiModelProperty("用户ID")
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty("新密码")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ApiModelProperty("旧密码")
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
