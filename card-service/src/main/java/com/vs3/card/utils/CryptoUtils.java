package com.vs3.card.utils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {

    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_SIZE = 12;

    private static final byte[] SECRET_KEY = 
        Base64.getDecoder().decode("X9N7vZ6TXQ1xkKqXUjBf87XjHk3Wb4H2v3CqJ1p9VxY=");

    private static final SecureRandom secureRandom = new SecureRandom();

    public static SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY, AES);
    }

    public static String encrypt(String plainText, String ivBase64) throws Exception {
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherTextBase64, String ivBase64) throws Exception {
        byte[] cipherBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec);
        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes);
    }

    public static String randomIV() {
        byte[] iv = new byte[IV_SIZE];
        secureRandom.nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }
}

