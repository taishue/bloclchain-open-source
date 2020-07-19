package com.coinsthai.model;

import com.coinsthai.model.converter.BillStatusConverter;
import com.coinsthai.model.converter.BillTypeConverter;
import com.coinsthai.pojo.BillPojo;
import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_bill")
public class Bill extends BillPojo {

    private User user;

    private Market market;

    public Bill() {
    }

    public Bill(Long price, Long remainVolume) {
        this();

        if (price == null) {
            setPrice(0l);
        }
        else {
            setPrice(price);
        }

        if (remainVolume == null) {
            setRemainVolume(0l);
        }
        else {
            setRemainVolume(remainVolume);
        }
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
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
    @JoinColumn(name = "market_id")
    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
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
    public long getRemainVolume() {
        return super.getRemainVolume();
    }

    @Column
    @Override
    public long getPrice() {
        return super.getPrice();
    }

    @Column
    @Override
    public long getAveragePrice() {
        return super.getAveragePrice();
    }

    @Column
    @Override
    public long getDealValue() {
        return super.getDealValue();
    }

    @Column(nullable = false, precision = 2, scale = 8)
    @Override
    public BigDecimal getBrokerageRate() {
        return super.getBrokerageRate();
    }

    @Column
    @Override
    public long getBrokerage() {
        return super.getBrokerage();
    }

    @Column
    @Convert(converter = BillTypeConverter.class)
    @Override
    public BillType getType() {
        return super.getType();
    }

    @Column
    @Convert(converter = BillStatusConverter.class)
    @Override
    public BillStatus getStatus() {
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
