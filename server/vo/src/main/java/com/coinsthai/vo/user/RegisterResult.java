package com.coinsthai.vo.user;

import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "注册结果")
public class RegisterResult extends StringIdentifier {

    @ApiModelProperty("注册请求ID，用于重发邮件")
    @Override
    public String getId() {
        return super.getId();
    }
}
