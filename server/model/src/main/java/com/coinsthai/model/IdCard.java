package com.coinsthai.model;

import com.coinsthai.model.converter.GenderConverter;
import com.coinsthai.pojo.IdCardPojo;
import com.coinsthai.pojo.intenum.Gender;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "t_id_card")
public class IdCard extends IdCardPojo {

    private User user;

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

    @Column
    @Override
    public String getName() {
        return super.getName();
    }

    @Column
    @Override
    public String getCardNumber() {
        return super.getCardNumber();
    }

    @Column
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Column
    @Override
    public Date getBirthday() {
        return super.getBirthday();
    }

    @Column
    @Convert(converter = GenderConverter.class)
    @Override
    public Gender getGender() {
        return super.getGender();
    }

    @Column
    @Override
    public String getRace() {
        return super.getRace();
    }

    @Column
    @Override
    public String getIssuedBy() {
        return super.getIssuedBy();
    }

    @Column
    @Override
    public Date getValidBeginAt() {
        return super.getValidBeginAt();
    }

    @Column
    @Override
    public Date getValidEndAt() {
        return super.getValidEndAt();
    }

    @Column
    @Override
    public String getFrontImageKey() {
        return super.getFrontImageKey();
    }

    @Column
    @Override
    public String getBackImageKey() {
        return super.getBackImageKey();
    }

    @Column
    @Override
    public String getHoldImageKey() {
        return super.getHoldImageKey();
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
