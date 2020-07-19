package com.coinsthai.vo.kyc;

import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;

/**
 * @author 
 */
@ApiModel(description = "Face ID Token")
public class FaceIdToken extends StringIdentifier {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
