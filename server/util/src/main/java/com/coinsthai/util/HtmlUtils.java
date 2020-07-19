package com.coinsthai.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
    
    // 定义script的正则表达式
    private static final Pattern PATTERN_SCRIPT =
                                                Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>",
                                                                Pattern.CASE_INSENSITIVE);
    
    // 定义style的正则表达式
    private static final Pattern PATTERN_STYLE =
                                               Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>",
                                                               Pattern.CASE_INSENSITIVE);
    
    // 定义HTML标签的正则表达式
    private static final Pattern PATTERN_HTML =
                                              Pattern.compile("<[^>]+>",
                                                              Pattern.CASE_INSENSITIVE);
    
    private HtmlUtils() {
    }
    
    /**
     * 过滤HTML标记<br/>
     * 移除script及style，移除<>
     * 
     * @param inputString
     * @return
     */
    public static String filterHtml(String inputString) {
        if (StringUtils.isBlank(inputString)) {
            return inputString;
        }
        
        String htmlStr = inputString;
        Matcher scriptMatcher = PATTERN_SCRIPT.matcher(htmlStr);
        htmlStr = scriptMatcher.replaceAll(""); // 过滤script标签
        
        Matcher styleMatcher = PATTERN_STYLE.matcher(htmlStr);
        htmlStr = styleMatcher.replaceAll(""); // 过滤style标签
        
        Matcher htmlMatcher = PATTERN_HTML.matcher(htmlStr);
        htmlStr = htmlMatcher.replaceAll(""); // 过滤html标签
        
        return htmlStr.trim(); // 返回文本字符串
    }
    
    /**
     * 将大于号、小于号转义
     * 
     * @param inputString
     * @return
     */
    public static String escapeHtml(String inputString) {
        if (StringUtils.isBlank(inputString)) {
            return inputString;
        }
        String result = inputString;
        result = StringUtils.replace(result, ">", "&gt;");
        result = StringUtils.replace(result, "<", "&lt;");
        return result;
    }
}
