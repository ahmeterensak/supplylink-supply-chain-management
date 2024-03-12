package com.aes.supplylinksupplychainmanagement.util;

public class SlugCreator {
    public static String createSlug(String name) {
        return name.toLowerCase().replaceAll("\\s+", "-");
    }
}
