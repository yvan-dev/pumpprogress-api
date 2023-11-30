package com.pumpprogress.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public class KeyGeneratorUtils {
    // Private constructor to prevent instantiation
    private KeyGeneratorUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
        return keyGen.generateKey();
    }
}
