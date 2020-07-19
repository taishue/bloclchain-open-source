package com.coinsthai.security;

import in.clouthink.daas.security.token.support.web.TokenResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从Authorization header中获取token<br/>
 * Created by
 */
public class BearerAuthorizationHeaderTokenResolver implements TokenResolver {
    
    private static final Log LOGGER = LogFactory.getLog(BearerAuthorizationHeaderTokenResolver.class);
    
    private static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    
    @Override
    public String resolve(HttpServletRequest request,
                          HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("AuthorizationHeader=" + authHeader);
        }
        
        if (StringUtils.isEmpty(authHeader)
            || authHeader.length() <= HEADER_AUTHORIZATION_PREFIX.length()) {
            return null;
        }
        
        return authHeader.substring(HEADER_AUTHORIZATION_PREFIX.length());
    }
}
