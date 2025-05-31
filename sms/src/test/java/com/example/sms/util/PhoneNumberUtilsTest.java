package com.example.sms.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberUtilsTest {

    @Test
    void isValidPhoneNumber_validNumbers_shouldReturnTrue() {
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("+61123456789"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("+64 212345678"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("(415) 555-2671"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("1234567890"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("+4915123456789"));
    }

    @Test
    void isValidPhoneNumber_invalidNumbers_shouldReturnFalse() {
        assertFalse(PhoneNumberUtils.isValidPhoneNumber(""));
        assertFalse(PhoneNumberUtils.isValidPhoneNumber("abcd1234"));
        assertFalse(PhoneNumberUtils.isValidPhoneNumber("12345"));
        assertFalse(PhoneNumberUtils.isValidPhoneNumber("++1234567890"));
    }

    @Test
    void routeCarrier_forPlus61_shouldAlternateBetweenTelstraAndOptus() {
        String number = "+61123456789";

        assertEquals("Optus", PhoneNumberUtils.routeCarrier(number, false));
        assertEquals("Telstra", PhoneNumberUtils.routeCarrier(number, true));
    }

    @Test
    void routeCarrier_forPlus64_shouldReturnSpark() {
        String number = "+64211234567";

        assertEquals("Spark", PhoneNumberUtils.routeCarrier(number, false));
        assertEquals("Spark", PhoneNumberUtils.routeCarrier(number, true));
    }

    @Test
    void routeCarrier_forOtherNumbers_shouldReturnGlobal() {
        String number = "+4915123456789";

        assertEquals("Global", PhoneNumberUtils.routeCarrier(number, false));
        assertEquals("Global", PhoneNumberUtils.routeCarrier(number, true));
    }
}
