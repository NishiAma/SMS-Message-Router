package com.example.sms.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberUtilsTest {

    @Test
    void testValidNumbers() {
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("+61123456789"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("+64 212345678"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("(415) 555-2671"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("1234567890"));
        assertTrue(PhoneNumberUtils.isValidPhoneNumber("+4915123456789"));
    }

    @Test
    void testInvalidNumbers() {
        assertFalse(PhoneNumberUtils.isValidPhoneNumber(""));
        assertFalse(PhoneNumberUtils.isValidPhoneNumber("abcd1234"));
        assertFalse(PhoneNumberUtils.isValidPhoneNumber("12345"));
        assertFalse(PhoneNumberUtils.isValidPhoneNumber("++1234567890"));
    }

    @Test
    void testAlternateCarriersForAu() {
        String number = "+61123456789";

        assertEquals("Optus", PhoneNumberUtils.routeCarrier(number, false));
        assertEquals("Telstra", PhoneNumberUtils.routeCarrier(number, true));
    }

    @Test
    void testNZPhoneNumbers() {
        String number = "+64211234567";

        assertEquals("Spark", PhoneNumberUtils.routeCarrier(number, false));
        assertEquals("Spark", PhoneNumberUtils.routeCarrier(number, true));
    }

    @Test
    void testGlobalPhoneNumbers() {
        String number = "+4915123456789";

        assertEquals("Global", PhoneNumberUtils.routeCarrier(number, false));
        assertEquals("Global", PhoneNumberUtils.routeCarrier(number, true));
    }
}
