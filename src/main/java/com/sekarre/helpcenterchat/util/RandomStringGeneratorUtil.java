package com.sekarre.helpcenterchat.util;

import java.util.UUID;

public class RandomStringGeneratorUtil {

    public static String getRandomString() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public static String getRandomString(Integer length) {
        int maxLength = UUID.randomUUID().toString().length();
        if (length > UUID.randomUUID().toString().length()) {
            length = maxLength;
        }
        return UUID.randomUUID().toString().substring(0, length);
    }
}
