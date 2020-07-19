package com.coinsthai.security;

import in.clouthink.daas.security.token.support.web.DefaultAuthorizationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 
 */
public class AuthorizationFailureHandler extends
                                         DefaultAuthorizationFailureHandler {
                                         
    private String loginUrl = "/login";
    
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       Exception exception) throws IOException,
                                                   ServletException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.indexOf("json") > -1) {
            super.handle(request, response, exception);
        }
        else {
            redirectLogin(response);
        }
    }
    
    private void redirectLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl);
        response.flushBuffer();
    }
}
