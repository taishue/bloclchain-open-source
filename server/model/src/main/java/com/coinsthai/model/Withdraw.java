package com.coinsthai.model;

import com.coinsthai.model.converter.WithdrawStatusConverter;
import com.coinsthai.pojo.WithdrawPojo;
import com.coinsthai.pojo.intenum.WithdrawStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_withdraw")
public class Withdraw extends WithdrawPojo {

    private Wallet wallet;

    private User user;

    private Coin coin;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    @Column
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Column
    @Override
    public long getBrokerage() {
        return super.getBrokerage();
    }

    @Column
    @Override
    public String getTxid() {
        return super.getTxid();
    }

    @Column
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @Column
    @Override
    public long getVolume() {
        return super.getVolume();
    }

    @Column
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @Column
    @Convert(converter = WithdrawStatusConverter.class)
    @Override
    public WithdrawStatus getStatus() {
        return super.getStatus();
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
