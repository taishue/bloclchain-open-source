package com.coinsthai.eth.impl;

import com.coinsthai.pojo.blockchain.EthTransactionPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtHTransactionSyncView extends EthTransactionPojo {

    @JsonProperty("txreceipt_status")
    @Override
    public String getTxReceiptStatus() {
        return super.getTxReceiptStatus();
    }

    @JsonProperty("from")
    @Override
    public String getFromAddress() {
        return super.getFromAddress();
    }

    @JsonProperty("to")
    @Override
    public String getToAddress() {
        return super.getToAddress();
    }

    @JsonProperty("hash")
    @Override
    public String getTxHash() {
        return super.getTxHash();
    }

    @JsonProperty("value")
    @Override
    public String getTxValue() {
        return super.getTxValue();
    }
}
