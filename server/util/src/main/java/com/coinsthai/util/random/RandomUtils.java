package com.coinsthai.util.random;

import com.coinsthai.util.CommonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成随机数或字符串等的工具类<br/>
 * 请结合{@link RandomStringUtils} 使用 <br/>
 * Created by
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {

    private RandomUtils() {
    }

    public static boolean randomBoolean() {
        return org.apache.commons.lang3.RandomUtils.nextInt(0, 2) == 1;
    }

    public static <T extends Enum<T>> T randomEnum(Class<T> ec) {
        return randomObject(ec.getEnumConstants());
    }

    public static <T> T randomObject(T[] values) {
        Validate.isTrue(values != null && values.length > 0,
                        "Values cannot be null nor empty.");
        return values[nextInt(0, values.length)];
    }

    public static List<String> randomStrList(final int countStart,
                                             final int countEnd,
                                             boolean id) {
        List list = new ArrayList<String>();
        int num = nextInt(countStart, countEnd);
        for (int i = 0; i < num; i++) {
            if (id) {
                list.add(CommonUtils.simpleUUID());
            }
            else {
                list.add(randomString(5, 10));
            }
        }
        return list;
    }

    public static <T> T randomObject(List<T> values) {
        Validate.isTrue(values != null && !values.isEmpty(),
                        "Values cannot be null nor empty.");
        return values.get(nextInt(0, values.size()));
    }

    public static String randomString(final int countStart,
                                      final int countEnd) {
        int count = nextInt(countStart, countEnd);
        return RandomStringUtils.random(count);
    }

    public static String randomAscii(final int countStart, final int countEnd) {
        int count = nextInt(countStart, countEnd);
        return RandomStringUtils.randomAscii(count);
    }

    public static String randomAlphabetic(final int countStart,
                                          final int countEnd) {
        int count = nextInt(countStart, countEnd);
        return RandomStringUtils.randomAlphabetic(count);
    }

    public static String randomAlphanumeric(final int countStart,
                                            final int countEnd) {
        int count = nextInt(countStart, countEnd);
        return RandomStringUtils.randomAlphanumeric(count);
    }

    public static String randomNumeric(final int countStart,
                                       final int countEnd) {
        int count = nextInt(countStart, countEnd);
        return RandomStringUtils.randomNumeric(count);
    }

    public static Date randomDate(Date begin, Date end) {
        if (begin.getTime() >= end.getTime()) {
            return null;
        }
        long date = randomlong(begin.getTime(), end.getTime());
        return new Date(date);
    }

    public static long randomlong(long begin, long end) {
        long rtnn = begin + (long) (Math.random() * (end - begin));
        if (rtnn == begin || rtnn == end) {
            return randomlong(begin, end);
        }
        return rtnn;
    }

    public static BigDecimal randomMoney(int min, int max) {
        int first = nextInt(min, max);
        int last = 0;
        if (first < max) {
            last = nextInt(0, 99);
        }
        return new BigDecimal(first + "." + last);
    }

}
