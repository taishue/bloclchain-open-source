package com.coinsthai.eth.impl;

import java.util.List;

/**
 * @author YeYifeng
 */
public class EthTransactionResponse {

    private int status;

    private String message;

    List<EtHTransactionSyncView> result;

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

    public List<EtHTransactionSyncView> getResult() {
        return result;
    }

    public void setResult(List<EtHTransactionSyncView> result) {
        this.result = result;
    }
}
