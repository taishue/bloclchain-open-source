package com.coinsthai.eth.impl;

import java.util.List;

/**
 * @author YeYifeng
 */
public class EthERC20TransactionResponse {

    private int status;

    private String message;

    List<EthERC20TransactionSyncView> result;

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

    public List<EthERC20TransactionSyncView> getResult() {
        return result;
    }

    public void setResult(List<EthERC20TransactionSyncView> result) {
        this.result = result;
    }
}
