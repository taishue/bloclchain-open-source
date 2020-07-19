package com.coinsthai.pojo.blockchain;

import com.coinsthai.pojo.common.ModifiedAtPojo;

import java.math.BigDecimal;

/**
 * @author 
 */
public class BitcoinTransactionItemPojo extends ModifiedAtPojo {

    public static final String CATEGORY_RECEIVE = "receive";

    public static final String CATEGORY_SEND = "send";

    private String txid;

    private String address;

    private String category;

    private BigDecimal amount;

    private BigDecimal fee;

    private int vout;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public int getVout() {
        return vout;
    }

    public void setVout(int vout) {
        this.vout = vout;
    }
}
