package com.vs3.card.utils;

public class PanValidator {

    public static boolean isValidPan(String pan) {
        if (!pan.matches("\\d{12,19}")) return false;
        return luhnCheck(pan);
    }

    private static boolean luhnCheck(String pan) {
        int sum = 0;
        boolean alternate = false;
        for (int i = pan.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(pan.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
