package com.coinsthai.security;

import com.coinsthai.model.User;
import com.coinsthai.service.UserService;
import com.coinsthai.util.WebUtils;
import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.support.web.DefaultAuthenticationSuccessHandler;
import in.clouthink.daas.security.token.support.web.WebResultWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author
 */
public class AuthenticationSuccessHandler extends DefaultAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException,
                                                                              ServletException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cache-Control", "no-store");
            headers.set("Pragma", "no-cache");

            Token currentToken = authentication.currentToken();
            saveLogin(currentToken.getOwner().getId(), request);

            String tokenString = currentToken.getToken();
            ResponseEntity result = new ResponseEntity(WebResultWrapper.succeedMap("token", tokenString),
                                                       headers,
                                                       HttpStatus.OK);
            getRenderer().handleResponse(result, request, response);
            response.flushBuffer();
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Wrap other Exceptions. These are not expected to happen
            throw new ServletException(e);
        }
    }

    // 记录登录时间和IP
    private void saveLogin(String userId, HttpServletRequest request) {
        try {
            User user = userService.get(userId);
            user.setLastLoggedAt(new Date());
            user.setLastLoggedIp(WebUtils.getIp(request));
            userService.update(user);
        } catch (Exception e) {
            LOGGER.error("Failed to save last login info for user.", e);
        }
    }

}
