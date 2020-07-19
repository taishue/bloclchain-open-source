package com.coinsthai.vo;

import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "验证码发送结果")
public class PasscodeResponse extends StringIdentifier {

    @ApiModelProperty("验证码ID，用于重发邮件")
    @Override
    public String getId() {
        return super.getId();
    }

}
