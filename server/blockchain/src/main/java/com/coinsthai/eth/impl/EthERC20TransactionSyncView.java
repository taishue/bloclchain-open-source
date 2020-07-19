package com.coinsthai.eth.impl;

import com.coinsthai.pojo.blockchain.EthERC20TransactionPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author YeYifeng
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EthERC20TransactionSyncView extends EthERC20TransactionPojo {

    @JsonProperty("hash")
    @Override
    public String getTxHash() {
        return super.getTxHash();
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

    @JsonProperty("value")
    @Override
    public String getTxValue() {
        return super.getTxValue();
    }
}
