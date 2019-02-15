package com.wakawala.helpers;

public class ValidationHelper {

    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean isValidEmail(String email) {
        return !email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidMobileNumber(String mobile) {
        return mobile.length() != 10;
    }
}
