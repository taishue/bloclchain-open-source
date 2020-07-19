package com.coinsthai.module.kyc;

import com.coinsthai.pojo.common.StringIdentifier;

/**
 * @author
 */
public class FaceIdTokenRequest extends StringIdentifier {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
