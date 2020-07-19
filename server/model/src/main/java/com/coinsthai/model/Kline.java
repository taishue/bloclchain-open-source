package com.coinsthai.model;

import com.coinsthai.model.converter.KlineTypeConverter;
import com.coinsthai.pojo.KlinePojo;
import com.coinsthai.pojo.intenum.KlineType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_kline")
public class Kline extends KlinePojo {

    private Market market;

    public Kline() {
    }

    public Kline(Long high, Long low, Long volume) {
        this();
        if (high == null) {
            setHigh(0l);
        }
        else {
            setHigh(high);
        }

        if (low == null) {
            setLow(0l);
        }
        else {
            setLow(low);
        }

        if (volume == null) {
            setVolume(0l);
        }
        else {
            setVolume(volume);
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
    @JoinColumn(name = "market_id")
    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @Column
    @Convert(converter = KlineTypeConverter.class)
    @Override
    public KlineType getType() {
        return super.getType();
    }

    @Column
    @Override
    public long getTimestamp() {
        return super.getTimestamp();
    }

    @Column
    @Override
    public long getFirst() {
        return super.getFirst();
    }

    @Column
    @Override
    public long getLast() {
        return super.getLast();
    }

    @Column
    @Override
    public long getHigh() {
        return super.getHigh();
    }

    @Column
    @Override
    public long getLow() {
        return super.getLow();
    }

    @Column
    @Override
    public long getVolume() {
        return super.getVolume();
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
