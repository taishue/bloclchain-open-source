package com.coinsthai.security;

import com.coinsthai.pojo.UserPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.clouthink.daas.security.token.core.Role;
import in.clouthink.daas.security.token.core.User;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityUser extends UserPojo implements User {

    private List<Role> roles;

    private String password;

    private String passwordSalt;

    private String avatar;

    @Override
    public String getUsername() {
        if (StringUtils.isNotBlank(getEmail())) {
            return getEmail();
        }
        return getCellphone();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public boolean isExpired() {
        return !isActive();
    }

    @Override
    public boolean isLocked() {
        return !isActive();
    }

    @Override
    public List<Role> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
