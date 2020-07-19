package com.coinsthai.util.random;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by
 */
public class RandomBoolean {

    private RandomBoolean() {
    }

    public static boolean random() {
        return RandomUtils.nextInt(0, 2) == 1;
    }

}
