package com.coinsthai.btc.impl;

import com.coinsthai.pojo.blockchain.BtcTransactionPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author YeYifeng
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BtcTransactionSyncView extends BtcTransactionPojo {

    private List<BtcTransactionInput> inputs;

    private List<BtcTransactionOut> out;

    public List<BtcTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<BtcTransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<BtcTransactionOut> getOut() {
        return out;
    }

    public void setOut(List<BtcTransactionOut> out) {
        this.out = out;
    }

    @JsonProperty("block_height")
    @Override
    public int getBlockHeight() {
        return super.getBlockHeight();
    }

    @JsonProperty("relayed_by")
    @Override
    public String getRelayedBy() {
        return super.getRelayedBy();
    }

    @JsonProperty("lock_time")
    @Override
    public long getLockTime() {
        return super.getLockTime();
    }

    @JsonProperty("time")
    @Override
    public long getTxTime() {
        return super.getTxTime();
    }

    @JsonProperty("tx_index")
    @Override
    public long getTxIndex() {
        return super.getTxIndex();
    }

    @JsonProperty("vin_sz")
    @Override
    public int getVinSz() {
        return super.getVinSz();
    }

    @JsonProperty("hash")
    @Override
    public String getTxHash() {
        return super.getTxHash();
    }

    @JsonProperty("vout_sz")
    @Override
    public int getVoutSz() {
        return super.getVoutSz();
    }
}
