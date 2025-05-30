package com.example.sms.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageStatusTest {

    @Test
    public void testEnumValues() {
        assertEquals("PENDING", MessageStatus.PENDING.name());
        assertEquals("SENT", MessageStatus.SENT.name());
        assertEquals("DELIVERED", MessageStatus.DELIVERED.name());
        assertEquals("BLOCKED", MessageStatus.BLOCKED.name());
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, MessageStatus.PENDING.ordinal());
        assertEquals(1, MessageStatus.SENT.ordinal());
        assertEquals(2, MessageStatus.DELIVERED.ordinal());
        assertEquals(3, MessageStatus.BLOCKED.ordinal());
    }
}
