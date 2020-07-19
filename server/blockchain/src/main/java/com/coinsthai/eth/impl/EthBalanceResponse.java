package com.coinsthai.eth.impl;

import java.util.List;

/**
 * @author YeYifeng
 */
public class EthBalanceResponse {

    private int status;

    private String message;

    private String result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
