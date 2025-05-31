package com.example.sms.util;

public class PhoneNumberUtils {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty())
            return false;

        String cleanPhoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");

        if (cleanPhoneNumber.matches("^\\+?\\d{10,15}$")) {
            return true;
        }

        return false;
    }

    public static String routeCarrier(String phoneNumber, boolean alternateCarrier) {
        if (phoneNumber.startsWith("+61")) {
            return alternateCarrier ? "Telstra" : "Optus";
        } else if (phoneNumber.startsWith("+64")) {
            return "Spark";
        } else {
            return "Global";
        }
    }

}
