package com.coinsthai.model;

import com.coinsthai.model.converter.WalletLogTypeConverter;
import com.coinsthai.pojo.WalletLogPojo;
import com.coinsthai.pojo.intenum.WalletLogType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_wallet_log")
public class WalletLog extends WalletLogPojo {

    private Wallet wallet;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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

    @Column
    @Override
    public long getVolume() {
        return super.getVolume();
    }

    @Column
    @Override
    public long getAvailableBalance() {
        return super.getAvailableBalance();
    }

    @Column
    @Override
    public long getFrozenBalance() {
        return super.getFrozenBalance();
    }

    @Column
    @Override
    public long getPendingBalance() {
        return super.getPendingBalance();
    }

    @Column
    @Convert(converter = WalletLogTypeConverter.class)
    @Override
    public WalletLogType getType() {
        return super.getType();
    }

    @Column
    @Override
    public String getBizId() {
        return super.getBizId();
    }

    @Column
    @Override
    public String getDescription() {
        return super.getDescription();
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
