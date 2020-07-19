package com.coinsthai.model.blockchain;

import com.coinsthai.model.ModelUtils;
import com.coinsthai.pojo.blockchain.BitcoinTransactionPojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "bch_transaction")
public class BchTransaction extends BitcoinTransactionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @Column
    @Override
    public String getHash() {
        return super.getHash();
    }

    @Column
    @Override
    public int getConfirmations() {
        return super.getConfirmations();
    }

    @Column
    @Override
    public String getBlockHash() {
        return super.getBlockHash();
    }

    @Column
    @Override
    public int getBlockIndex() {
        return super.getBlockIndex();
    }

    @Column
    @Override
    public Date getBlockTime() {
        return super.getBlockTime();
    }

    @Column
    @Override
    public Date getTime() {
        return super.getTime();
    }

    @Column
    @Override
    public Date getTimeReceived() {
        return super.getTimeReceived();
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
