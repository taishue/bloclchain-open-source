package com.coinsthai.model;

import com.coinsthai.model.converter.CoinCategoryConverter;
import com.coinsthai.pojo.CoinPojo;
import com.coinsthai.pojo.intenum.CoinCategory;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_coin")
public class Coin extends CoinPojo {

    private Coin tokenOn;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_on")
    public Coin getTokenOn() {
        return tokenOn;
    }

    public void setTokenOn(Coin tokenOn) {
        this.tokenOn = tokenOn;
    }

    @Column
    @Convert(converter = CoinCategoryConverter.class)
    @Override
    public CoinCategory getCategory() {
        return super.getCategory();
    }

    @Column
    @Override
    public String getName() {
        return super.getName();
    }

    @Column
    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @Column
    @Override
    public boolean isBase() {
        return super.isBase();
    }

    @Column
    @Override
    public String getContract() {
        return super.getContract();
    }

    @Column
    @Override
    public int getUnit() {
        return super.getUnit();
    }


    @Column
    @Override
    public long getMinDeal() {
        return super.getMinDeal();
    }

    @Column
    @Override
    public long getMinDealBrokerage() {
        return super.getMinDealBrokerage();
    }

    @Column
    @Override
    public long getMinWithdraw() {
        return super.getMinWithdraw();
    }

    @Column
    @Override
    public long getMinNetworkBrokerage() {
        return super.getMinNetworkBrokerage();
    }

    @Column
    @Override
    public long getMaxNetworkBrokerage() {
        return super.getMaxNetworkBrokerage();
    }

    @Column
    @Override
    public int getPriority() {
        return super.getPriority();
    }

    @Column
    @Override
    public boolean isActive() {
        return super.isActive();
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
