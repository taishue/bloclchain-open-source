package com.coinsthai.model.blockchain;

import com.coinsthai.model.ModelUtils;
import com.coinsthai.pojo.blockchain.EthTransactionPojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YeYifeng
 */
@Entity
@Table(name = "t_eth_transaction")
public class EthTransaction extends EthTransactionPojo {

    @Id
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @Column
    @Override
    public long getBlockNumber() {
        return super.getBlockNumber();
    }

    @Column
    @Override
    public long getTimeStamp() {
        return super.getTimeStamp();
    }

    @Column
    @Override
    public int getNonce() {
        return super.getNonce();
    }

    @Column
    @Override
    public int getTransactionIndex() {
        return super.getTransactionIndex();
    }

    @Column
    @Override
    public String getGas() {
        return super.getGas();
    }

    @Column
    @Override
    public String getGasPrice() {
        return super.getGasPrice();
    }

    @Column
    @Override
    public int getIsError() {
        return super.getIsError();
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    @Override
    public String getInput() {
        return super.getInput();
    }

    @Column
    @Override
    public String getContractAddress() {
        return super.getContractAddress();
    }

    @Column
    @Override
    public String getCumulativeGasUsed() {
        return super.getCumulativeGasUsed();
    }

    @Column
    @Override
    public String getGasUsed() {
        return super.getGasUsed();
    }

    @Column
    @Override
    public int getConfirmations() {
        return super.getConfirmations();
    }

    @Column
    @Override
    public String getTxHash() {
        return super.getTxHash();
    }

    @Column
    @Override
    public String getBlockHash() {
        return super.getBlockHash();
    }

    @Column
    @Override
    public String getFromAddress() {
        return super.getFromAddress();
    }

    @Column
    @Override
    public String getToAddress() {
        return super.getToAddress();
    }

    @Column
    @Override
    public String getTxValue() {
        return super.getTxValue();
    }

    @Column
    @Override
    public String getTxReceiptStatus() {
        return super.getTxReceiptStatus();
    }

    @Column
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @Column
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @PrePersist
    protected void prePersist() {
        ModelUtils.prePersist(this);
    }

    @PreUpdate
    protected void preUpdate() {
        ModelUtils.preUpdate(this);
    }

}
