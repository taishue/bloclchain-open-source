package com.coinsthai.service;

import com.coinsthai.model.User;
import com.coinsthai.vo.user.RegisterRequest;
import com.coinsthai.vo.user.RegisterResult;

/**
 * @author 
 */
public interface RegisterService {

    RegisterResult register(RegisterRequest request);

    /**
     * 发送激活邮件
     *
     * @param registerId
     */
    void sendActiveEmail(String registerId);

    /**
     * 通知重发激活邮件
     *
     * @param registerId
     */
    void resendActiveEmail(String registerId);

    User activeUser(String registerId, String activeCode);
}
