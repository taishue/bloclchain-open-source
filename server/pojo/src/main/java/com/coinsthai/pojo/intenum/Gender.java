package com.coinsthai.pojo.intenum;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 
 */
public enum Gender implements IntEnum {

    MALE(0),     //男
    FEMALE(1);     //女

    private int number;

    Gender(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public static Gender parse(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        if (MALE.name().equalsIgnoreCase(str) || "男".equals(str)) {
            return MALE;
        }
        if (FEMALE.name().equalsIgnoreCase(str) || "女".equals(str)) {
            return FEMALE;
        }

        return null;
    }
}
