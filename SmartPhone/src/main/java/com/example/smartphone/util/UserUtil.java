package com.example.smartphone.util;

public class UserUtil {
    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserUtil.username = username;
    }
}
