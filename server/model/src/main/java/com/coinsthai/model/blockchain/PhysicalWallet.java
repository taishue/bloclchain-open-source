package com.coinsthai.model.blockchain;

import com.coinsthai.model.ModelUtils;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.blockchain.PhysicalWalletPojo;
import com.coinsthai.pojo.blockchain.CoinType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 
 */
@Entity
@Table(name = "t_physical_wallet")
public class PhysicalWallet extends PhysicalWalletPojo {

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

    @Enumerated(EnumType.STRING)
    @Column
    @Override
    public CoinType getCoinType() {
        return super.getCoinType();
    }

    @Column
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Column
    @Override
    public long getBalance() {
        return super.getBalance();
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
