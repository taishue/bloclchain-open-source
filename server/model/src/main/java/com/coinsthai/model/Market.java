package com.coinsthai.model;

import com.coinsthai.pojo.MarketPojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_market")
public class Market extends MarketPojo {

    private Coin target;

    private Coin base;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    public Coin getTarget() {
        return target;
    }

    public void setTarget(Coin target) {
        this.target = target;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_id")
    public Coin getBase() {
        return base;
    }

    public void setBase(Coin base) {
        this.base = base;
    }

    @Column
    @Override
    public String getName() {
        return super.getName();
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
    public boolean isActive() {
        return super.isActive();
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
