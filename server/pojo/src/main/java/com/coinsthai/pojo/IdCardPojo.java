package com.coinsthai.pojo;

import com.coinsthai.pojo.common.NamedModifiedAtPojo;
import com.coinsthai.pojo.intenum.Gender;

import java.util.Date;

/**
 * @author
 */
public class IdCardPojo extends NamedModifiedAtPojo {

    private String cardNumber;

    private String address;

    private Date birthday;

    private Gender gender;

    private String race;

    private String issuedBy;

    private Date validBeginAt;

    private Date validEndAt;

    private String frontImageKey;

    private String backImageKey;

    private String holdImageKey;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getValidBeginAt() {
        return validBeginAt;
    }

    public void setValidBeginAt(Date validBeginAt) {
        this.validBeginAt = validBeginAt;
    }

    public Date getValidEndAt() {
        return validEndAt;
    }

    public void setValidEndAt(Date validEndAt) {
        this.validEndAt = validEndAt;
    }

    public String getFrontImageKey() {
        return frontImageKey;
    }

    public void setFrontImageKey(String frontImageKey) {
        this.frontImageKey = frontImageKey;
    }

    public String getBackImageKey() {
        return backImageKey;
    }

    public void setBackImageKey(String backImageKey) {
        this.backImageKey = backImageKey;
    }

    public String getHoldImageKey() {
        return holdImageKey;
    }

    public void setHoldImageKey(String holdImageKey) {
        this.holdImageKey = holdImageKey;
    }
}
