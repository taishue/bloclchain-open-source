package com.coinsthai.security;

import com.coinsthai.util.CookieUtils;
import com.coinsthai.util.WebUtils;
import in.clouthink.daas.security.token.support.web.TokenResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 
 */
public class CookieTokenResolver implements TokenResolver {

    private static final Log LOGGER =
            LogFactory.getLog(CookieTokenResolver.class);

    @Override
    public String resolve(HttpServletRequest request,
                          HttpServletResponse response) {
        String token = CookieUtils.getCookie(request, WebUtils.AUTH_COOKIE);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("AuthCookie=" + token);
        }

        return token;
    }
}
