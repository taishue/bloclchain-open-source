package com.coinsthai.security;

import in.clouthink.daas.security.token.support.web.TokenResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 
 */
public class CompositeTokenResolver implements TokenResolver {
    
    private static final Log LOGGER = LogFactory.getLog(CompositeTokenResolver.class);
    
    private List<TokenResolver> resolvers;
    
    public List<TokenResolver> getResolvers() {
        if (resolvers == null) {
            resolvers = new ArrayList<>();
        }
        return resolvers;
    }
    
    public void setResolvers(List<TokenResolver> resolvers) {
        this.resolvers = resolvers;
    }
    
    @Override
    public String resolve(HttpServletRequest request,
                          HttpServletResponse response) {
        if (resolvers == null || resolvers.isEmpty()) {
            return null;
        }
        
        String token = null;
        for (TokenResolver resolver : resolvers) {
            try {
                token = resolver.resolve(request, response);
            }
            catch (Exception e) {
                LOGGER.error("Failed to resolve token.", e);
                continue;
            }
            
            if (token != null) {
                break;
            }
        }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("token=" + token);
        }
        
        if (StringUtils.isBlank(token)) {
            token = null;
        }
        return token;
    }
    
}
