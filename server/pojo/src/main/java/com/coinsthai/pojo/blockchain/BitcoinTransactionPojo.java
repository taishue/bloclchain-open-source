package com.coinsthai.pojo.blockchain;

import com.coinsthai.pojo.common.ModifiedAtPojo;

import java.util.Date;

/**
 * @author 
 */
public class BitcoinTransactionPojo extends ModifiedAtPojo {

    // hash of transaction
    private String hash;

    private int confirmations;

    private String blockHash;

    // The index of the transaction in the block
    private int blockIndex;

    // The block header time (Unix epoch time)
    private Date blockTime;

    // A Unix epoch time when the transaction was added to the wallet
    private Date time;

    // A Unix epoch time when the transaction was detected by the local node,
    // or the time of the block on the local best block chain
    private Date timeReceived;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(Date timeReceived) {
        this.timeReceived = timeReceived;
    }
}
