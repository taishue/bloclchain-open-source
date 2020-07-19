package com.coinsthai.module.passcode;

import com.coinsthai.pojo.common.StringIdentifier;

/**
 * @author
 */
public class PasscodeRequest extends StringIdentifier {

    private String receiverId;

    /**
     * 获取验证码的操作名称key，定义在资源文件中
     */
    private String operation;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
