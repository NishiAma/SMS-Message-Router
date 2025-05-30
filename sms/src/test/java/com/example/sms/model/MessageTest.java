package com.example.sms.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageInitialization() {
        String destinationNumber = "+61491570156";
        String content = "Hello";
        String format = "SMS";

        Message message = new Message(destinationNumber, content, format);

        assertNotNull(message.getId(), "ID should not be null");
        assertEquals(destinationNumber, message.getDestinationNumber());
        assertEquals(content, message.getContent());
        assertEquals(format, message.getFormat());
        assertEquals(MessageStatus.PENDING, message.getStatus(), "Default status should be PENDING");
        assertNull(message.getCarrier(), "Carrier should be null by default");
    }

    @Test
    public void testSettersAndGetters() {
        Message message = new Message("+61491570156", "Hello", "SMS");

        message.setCarrier("Telstra");
        message.setStatus(MessageStatus.DELIVERED);

        assertEquals("Telstra", message.getCarrier());
        assertEquals(MessageStatus.DELIVERED, message.getStatus());
    }
}
