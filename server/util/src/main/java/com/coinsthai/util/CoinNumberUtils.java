package com.coinsthai.util;

import java.math.BigDecimal;

/**
 * @author
 */
public class CoinNumberUtils {

    /**
     * 将货币的unit转换为小数位数
     *
     * @param unit 10的指数，如10, 100, 100000等
     * @return
     */
    public static int toDecimalLength(int unit) {
        return String.valueOf(unit).length() - 1;
    }

    /**
     * 将double四舍五入后格式化
     *
     * @param value         double值
     * @param decimalLength 保留的小数位数
     * @return
     */
    public static double formatDouble(double value, int decimalLength) {
        BigDecimal bd = new BigDecimal(String.valueOf(value));
        return formatDouble(bd, decimalLength);
    }

    /**
     * 将double四舍五入后格式化
     *
     * @param value         BigDecimal数值
     * @param decimalLength 保留的小数位数
     * @return
     */
    public static double formatDouble(BigDecimal value, int decimalLength) {
        return value.setScale(decimalLength, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将币种值格式化为double
     *
     * @param value 原long值
     * @param unit  货币单位
     * @return
     */
    public static double formatDoubleVolume(long value, int unit) {
        int decimalLength = toDecimalLength(unit);
        BigDecimal bd = (new BigDecimal(value)).divide(new BigDecimal(unit));
        double result = formatDouble(bd, decimalLength);
        checkNegative(result);
        return result;
    }

    /**
     * 将小数乘以unit，再四舍五入取整
     *
     * @param value
     * @param unit
     * @return
     */
    public static long parseLong(double value, int unit) {
        BigDecimal bdValue = new BigDecimal(String.valueOf(value));
        BigDecimal bdUnit = new BigDecimal(unit);
        BigDecimal bd = bdValue.multiply(bdUnit);
        long result = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        checkNegative(result);
        return result;
    }

    /**
     * 计算总价
     *
     * @param price  价格
     * @param volume 数量
     * @param unit   目标币种的unit
     * @return
     */
    public static long computeTotalPrice(long price, long volume, int unit) {
        BigDecimal priceBd = new BigDecimal(price);
        BigDecimal volumeBd = new BigDecimal(volume);
        BigDecimal unitBd = new BigDecimal(unit);
        BigDecimal resultBd = priceBd.multiply(volumeBd).divide(unitBd);
        long result = resultBd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        checkNegative(result);
        return result;
    }

    private static void checkNegative(double value) {
        if (value < 0d) {
            throw new RuntimeException("Wrong number.");
        }
    }

    private static void checkNegative(long value) {
        if (value < 0l) {
            throw new RuntimeException("Wrong number.");
        }
    }

    public static BigDecimal getDecimal(int zeros) {
        if (zeros <= 0) {
            throw new IllegalArgumentException("zeros must be greater than 0.");
        }
        StringBuilder sb = new StringBuilder("1");
        for (int i = 0; i < zeros; i++) {
            sb.append("0");
        }
        return new BigDecimal(sb.toString());
    }
}
