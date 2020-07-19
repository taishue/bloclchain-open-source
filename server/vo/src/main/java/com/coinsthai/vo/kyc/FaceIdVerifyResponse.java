package com.coinsthai.vo.kyc;

import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "FaceID验证结果")
public class FaceIdVerifyResponse extends StringIdentifier {

    private boolean verified;

    @ApiModelProperty(value = "是否已通过验证")
    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
