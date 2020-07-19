package com.coinsthai.model;

import com.coinsthai.pojo.UserPojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 
 */
@Entity
@Table(name = "t_user")
public class User extends UserPojo {

    private String password;

    private String passwordSalt;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    @Override
    public String getId() {
        return super.getId();
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column
    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Column
    @Override
    public String getName() {
        return super.getName();
    }

    @Column
    @Override
    public String getCellphone() {
        return super.getCellphone();
    }

    @Column
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Column
    @Override
    public String getLocale() {
        return super.getLocale();
    }

    @Column
    @Override
    public boolean isActive() {
        return super.isActive();
    }

    @Column
    @Override
    public boolean isRobot() {
        return super.isRobot();
    }

    @Column
    @Override
    public boolean isIdVerify() {
        return super.isIdVerify();
    }

    @Column
    @Override
    public boolean isBioVerify() {
        return super.isBioVerify();
    }

    @Column
    @Override
    public boolean isBankVerify() {
        return super.isBankVerify();
    }

    @Column
    @Override
    public Date getLastLoggedAt() {
        return super.getLastLoggedAt();
    }

    @Column
    @Override
    public String getLastLoggedIp() {
        return super.getLastLoggedIp();
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
