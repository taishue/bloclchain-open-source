package com.coinsthai.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author YeYifeng
 */
public class NumberConvertUtils {

    public static long convertToLocal(String originalValue, String originalUnit, int localUnit) {
        if (StringUtils.isEmpty(originalUnit)) {
            throw new IllegalArgumentException("originalUnit can't be empty.");
        }
        return convertToLocal(originalValue, new BigDecimal(originalUnit), localUnit);
    }

    public static long convertToLocal(String originalValue, BigDecimal originalUnit, int localUnit) {
        if (StringUtils.isEmpty(originalValue)) {
            throw new IllegalArgumentException("originalValue can't be empty.");
        }
        if (originalUnit == null) {
            throw new IllegalArgumentException("originalUnit can't be null.");
        }
        if (originalUnit.longValue() <= 0) {
            throw new IllegalArgumentException("originalUnit must be greater than 0.");
        }
        if (localUnit <= 0) {
            throw new IllegalArgumentException("localUnit must be greater than 0.");
        }
        BigDecimal dividend = new BigDecimal(originalValue);
        BigDecimal result = dividend.divide(originalUnit);
        BigDecimal unit = new BigDecimal(localUnit);
        BigDecimal localValue = result.multiply(unit);
        return localValue.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
    }
}
