package com.coinsthai.model.blockchain;

import com.coinsthai.model.ModelUtils;
import com.coinsthai.pojo.blockchain.BitcoinTransactionItemPojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "bch_transaction_item")
public class BchTransactionItem extends BitcoinTransactionItemPojo {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @Column
    @Override
    public String getTxid() {
        return super.getTxid();
    }

    @Column
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Column
    @Override
    public String getCategory() {
        return super.getCategory();
    }

    @Column
    @Override
    public BigDecimal getAmount() {
        return super.getAmount();
    }

    @Column
    @Override
    public BigDecimal getFee() {
        return super.getFee();
    }

    @Column
    @Override
    public int getVout() {
        return super.getVout();
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
