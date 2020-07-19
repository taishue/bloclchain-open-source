package com.coinsthai.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by
 */
public class CookieUtils {
    
    private CookieUtils() {
    }
    
    /**
     * 设置 Cookie, 过期时间为30分钟
     *
     * @param name
     *            名称
     * @param value
     *            值
     */
    public static Cookie setCookie(HttpServletResponse response,
                                   String name,
                                   String value,
                                   String path) {
        return addCookie(response, name, value, path, 60 * 30);
    }
    
    /**
     * 设置 Cookie, 过期时间自定义
     *
     * @param name
     *            名称
     * @param value
     *            值
     * @param maxAge
     *            过期时间, 单位秒
     */
    public static Cookie addCookie(HttpServletResponse response,
                                   String name,
                                   String value,
                                   String path,
                                   int maxAge) {
        return addCookie(response, name, value, null, path, maxAge);
    }
    
    /**
     * 设置 Cookie, 过期时间自定义
     *
     * @param name
     *            名称
     * @param value
     *            值
     * @param domain
     *            域名
     * @param path
     *            路径
     * @param maxAge
     *            过期时间, 单位秒
     */
    public static Cookie addCookie(HttpServletResponse response,
                                   String name,
                                   String value,
                                   String domain,
                                   String path,
                                   int maxAge) {
        Cookie cookie = null;
        try {
            if (value == null) {
                cookie = new Cookie(name, value);
            }
            else {
                cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            }
            cookie.setMaxAge(maxAge);
            if (null != path) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotBlank(domain)) {
                cookie.setDomain(domain);
            }
            response.addCookie(cookie);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookie;
    }
    
    /**
     * 设置 Cookies, 过期时间自定义
     *
     * @param response
     *            响应对象
     * @param values
     *            值
     * @param path
     *            路径
     * @param maxAge
     *            过期时间, 单位秒
     * @return Cookies
     */
    public static List<Cookie> addCookies(HttpServletResponse response,
                                          Map<String, String> values,
                                          String path,
                                          int maxAge) {
        Set<Map.Entry<String, String>> entries = values.entrySet();
        List<Cookie> cookies = new ArrayList<Cookie>();
        try {
            for (Map.Entry<String, String> entry : entries) {
                Cookie cookie = new Cookie(entry.getKey(),
                                           URLEncoder.encode(entry.getValue(),
                                                             "UTF-8"));
                cookie.setMaxAge(maxAge);
                if (null != path) {
                    cookie.setPath(path);
                }
                response.addCookie(cookie);
                cookies.add(cookie);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookies;
    }
    
    /**
     * 获得指定Cookie的值
     *
     * @param name
     *            名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, String name) {
        return getCookie(request, null, name, false);
    }
    
    /**
     * 获得指定Cookie的值，并删除。
     *
     * @param name
     *            名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String name) {
        return getCookie(request, response, name, true);
    }
    
    /**
     * 获得指定Cookie的值
     *
     * @param request
     *            请求对象
     * @param response
     *            响应对象
     * @param name
     *            名字
     * @param isRemoved
     *            是否移除
     * @return 值
     */
    public static String getCookie(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String name,
                                   boolean isRemoved) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    try {
                        value = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (isRemoved) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                    return value;
                }
            }
        }
        return value;
    }
}
