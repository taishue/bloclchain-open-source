package com.coinsthai.model;

import com.coinsthai.model.converter.BillTypeConverter;
import com.coinsthai.pojo.DealPojo;
import com.coinsthai.pojo.intenum.BillType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_deal")
public class Deal extends DealPojo {

    private Market market;

    // 卖单
    private Bill sell;

    // 买单
    private Bill buy;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_id")
    public Bill getSell() {
        return sell;
    }

    public void setSell(Bill sell) {
        this.sell = sell;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_id")
    public Bill getBuy() {
        return buy;
    }

    public void setBuy(Bill buy) {
        this.buy = buy;
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
    @Override
    public long getPrice() {
        return super.getPrice();
    }

    @Column
    @Override
    public long getSellBrokerage() {
        return super.getSellBrokerage();
    }

    @Column
    @Override
    public long getBuyBrokerage() {
        return super.getBuyBrokerage();
    }

    @Column
    @Convert(converter = BillTypeConverter.class)
    @Override
    public BillType getType() {
        return super.getType();
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
