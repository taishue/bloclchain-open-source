package com.coinsthai.pojo.blockchain;

import com.coinsthai.pojo.common.ModifiedAtPojo;

/**
 * @author YeYifeng
 */
public class BtcTransactionPojo extends ModifiedAtPojo {

    private int ver;

    private int weight;

    private int blockHeight;

    private String relayedBy;

    private long lockTime;

    private String result;

    private int size;

    private long txTime;

    private long txIndex;

    private int vinSz;

    private String txHash;

    private int voutSz;

    private String fromAddress;

    private String toAddress;

    private String txValue;

    private boolean confirmed;

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getRelayedBy() {
        return relayedBy;
    }

    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTxTime() {
        return txTime;
    }

    public void setTxTime(long txTime) {
        this.txTime = txTime;
    }

    public long getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(long txIndex) {
        this.txIndex = txIndex;
    }

    public int getVinSz() {
        return vinSz;
    }

    public void setVinSz(int vinSz) {
        this.vinSz = vinSz;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public int getVoutSz() {
        return voutSz;
    }

    public void setVoutSz(int voutSz) {
        this.voutSz = voutSz;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTxValue() {
        return txValue;
    }

    public void setTxValue(String txValue) {
        this.txValue = txValue;
    }
}
