package com.coinsthai.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by 
 */
public class WebUtils {
    
    public static final String SITE_NORMAL = "normal";
    
    public static final String SITE_TABLET = "tablet";
    
    public static final String SITE_MOBILE = "mobile";
    
    public static String SITE_PREFERENCE_COOKIE = "sitePreference";
    
    // 登录成功后应跳转的页面
    public static String LOGIN_FROM_COOKIE = "loginFrom";

    public static String AUTH_COOKIE = "auth";

    private WebUtils() {
    }
    
    /**
     * 获得不包含域名的请求URL，包含context path及query string
     * 
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getServletContext()
                                                     .getContextPath());
        url.append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            url.append('?').append(request.getQueryString());
        }
        return url.toString();
    }
    
    public static void renderHtml(HttpServletResponse response,
                                  String content) throws IOException {
        response.setHeader("Content-Type", "text/html");
        PrintWriter out = response.getWriter();
        out.print(content);
        out.flush();
    }
    
    /**
     * 判断是否静态资源
     * 
     * @param request
     * @return
     */
    public static boolean isStaticResource(HttpServletRequest request) {
        String re = request.getRequestURI();
        if (re.startsWith("/static") || re.startsWith("/js")
            || re.startsWith("/javascript")
            || re.startsWith("/css")
            || re.startsWith("/img")
            || re.startsWith("/image")) {
            return true;
        }
        return false;
    }
    
    /**
     * 获得IP地址
     * 
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 判断是否合法的URL
     * 
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        try {
            new URL(str);
        }
        catch (MalformedURLException e) {
            return false;
        }
        return true;
    }
    
}
