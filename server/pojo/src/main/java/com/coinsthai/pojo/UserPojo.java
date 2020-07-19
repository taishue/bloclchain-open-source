package com.coinsthai.pojo;

import com.coinsthai.pojo.common.NamedModifiedAtPojo;

import java.util.Date;

/**
 * @author 
 */
public class UserPojo extends NamedModifiedAtPojo {

    private String cellphone;

    private String email;

    private String locale;

    private boolean active = true;

    private boolean robot = false;

    private boolean idVerify;

    private boolean bioVerify;

    private boolean bankVerify;

    // 最后登录时间
    private Date lastLoggedAt;

    private String lastLoggedIp;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public Date getLastLoggedAt() {
        return lastLoggedAt;
    }

    public void setLastLoggedAt(Date lastLoggedAt) {
        this.lastLoggedAt = lastLoggedAt;
    }

    public String getLastLoggedIp() {
        return lastLoggedIp;
    }

    public void setLastLoggedIp(String lastLoggedIp) {
        this.lastLoggedIp = lastLoggedIp;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isIdVerify() {
        return idVerify;
    }

    public void setIdVerify(boolean idVerify) {
        this.idVerify = idVerify;
    }

    public boolean isBioVerify() {
        return bioVerify;
    }

    public void setBioVerify(boolean bioVerify) {
        this.bioVerify = bioVerify;
    }

    public boolean isBankVerify() {
        return bankVerify;
    }

    public void setBankVerify(boolean bankVerify) {
        this.bankVerify = bankVerify;
    }
}
