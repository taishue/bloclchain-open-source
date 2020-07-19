package com.coinsthai.security;

import in.clouthink.daas.security.token.core.Authentication;

/**
 * Created by
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获得当前登录用户信息
     *
     * @return
     */
    public static final SecurityUser currentUser() {
        Authentication authentication = in.clouthink.daas.security.token.support.SecurityUtils.currentAuthentication();
        if (authentication == null) {
            return null;
        }
        return (SecurityUser) authentication.currentToken().getOwner();
    }

    /**
     * 获得当前登录用户ID
     *
     * @return
     */
    public static final String currentUserId() {
        SecurityUser securityUser = currentUser();
        if (securityUser == null) {
            return null;
        }
        return securityUser.getId();
    }

}
