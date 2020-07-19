package com.coinsthai.module.passcode;

import com.coinsthai.module.passcode.PasscodeRequest;
import com.coinsthai.vo.PasscodeResponse;

/**
 * @author
 */
public interface PasscodeService {

    /**
     * 生成并发送验证码
     *
     * @param request
     * @return
     */
    PasscodeResponse request(PasscodeRequest request);

    /**
     * 通过邮件发送验证码
     *
     * @param id 验证码ID
     */
    void sendEmail(String id);

    /**
     * 验证码是否有效
     *
     * @param id     验证码ID
     * @param code   验证码
     * @param expire 如果验证码正确，是否将验证作失效处理
     * @return
     */
    boolean verify(String id, String code, boolean expire);
}
