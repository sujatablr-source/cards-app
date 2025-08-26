package com.vs3.card.utils;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class PanHasher {
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final byte[] HMAC_KEY = "super-secret-hmac-key".getBytes(); // vault in real life

    public static String hashPan(String pan) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(HMAC_KEY, HMAC_ALGO));
        byte[] result = mac.doFinal(pan.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }
}

