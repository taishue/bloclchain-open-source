package com.coinsthai.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 
 */
public class CommonUtils {

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final char[] SEPARATORS = new char[]{' ',
            '　',
            ',',
            '，',
            ';',
            '；',
            '\t',
            '\r',
            '\n'};

    private CommonUtils() {
    }

    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将字符串按照常用的多个分隔符分隔
     *
     * @param str
     * @return
     */
    public static List<String> splitBySeparators(String str) {
        List<String> list = new ArrayList<>();
        if (str != null && str.length() > 0) {
            List<Integer> starts = new ArrayList<>(); // 开始索引
            List<Integer> ends = new ArrayList<>(); // 结尾索引
            boolean gotStart = false;
            for (int i = 0; i < str.length(); i++) {
                if (isSeparator(str.charAt(i))) {
                    if (gotStart) {
                        ends.add(i);
                        gotStart = false;
                    }
                }
                else {
                    if (!gotStart) {
                        starts.add(i);
                        gotStart = true;
                    }
                }
            }
            if (gotStart) {
                ends.add(str.length());
            }

            for (int i = 0; i < starts.size(); i++) {
                list.add(str.substring(starts.get(i), ends.get(i)));
            }
        }
        return list;
    }

    private static boolean isSeparator(char ch) {
        for (char sep : SEPARATORS) {
            if (sep == ch) {
                return true;
            }
        }
        return false;
    }
}
