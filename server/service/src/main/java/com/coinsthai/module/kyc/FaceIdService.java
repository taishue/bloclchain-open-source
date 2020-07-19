package com.coinsthai.module.kyc;

import com.coinsthai.module.kyc.rest.FaceIdNotifyResultRaw;
import com.coinsthai.vo.kyc.FaceIdToken;

/**
 * @author
 */
public interface FaceIdService {

    /**
     * 从FaceID.com获取token
     *
     * @param request
     * @return
     */
    FaceIdToken requestToken(FaceIdTokenRequest request);

    /**
     * 响应FaceID.com的验证通知
     *
     * @param resultRaw
     * @return 是否通过验证
     */
    boolean handleNotify(FaceIdNotifyResultRaw resultRaw);

    /**
     * 根据验证请求ID获取用户是否已通过验证
     *
     * @param id
     * @return
     */
    boolean verify(String id, FaceIdNotifyResultRaw resultRaw);

    /**
     * 根据已通过的验证ID，获取照片等
     * @param id
     */
    void requestResult(String id);

}
