package com.example.sms.service;

import com.example.sms.model.Message;
import com.example.sms.model.MessageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService();
    }

    @Test
    void sendMessage_validNumber_shouldBeDelivered() {
        Message message = new Message("+61123456789", "Hello", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertNotNull(result.getCarrier());
    }

    @Test
    void sendMessage_invalidNumber_shouldFail() {
        Message message = new Message("invalid-number", "Hello", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals(MessageStatus.FAILED, result.getStatus());
        assertNull(result.getCarrier());
    }

    @Test
    void sendMessage_optedOutNumber_shouldBeBlocked() {
        String optedOutNumber = "+61123456789";
        messageService.optOut(optedOutNumber);

        Message message = new Message(optedOutNumber, "Hello", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals(MessageStatus.BLOCKED, result.getStatus());
        assertNull(result.getCarrier());
    }

    @Test
    void getMessage_shouldReturnStoredMessage() {
        Message message = new Message("+61123456789", "Hello", "SMS");
        messageService.sendMessage(message);

        Message retrieved = messageService.getMessage(message.getId());

        assertNotNull(retrieved);
        assertEquals("+61123456789", retrieved.getDestinationNumber());
        assertEquals(message, retrieved);
    }

    @Test
    void routeCarrier_shouldAlternateForPlus61Numbers() {
        Message message1 = new Message("+61123456789", "Hello 1", "SMS");
        Message message2 = new Message("+61123456789", "Hello 2", "SMS");

        Message result1 = messageService.sendMessage(message1);
        Message result2 = messageService.sendMessage(message2);

        // Should alternate between Telstra and Optus
        assertNotEquals(result1.getCarrier(), result2.getCarrier());
    }

    @Test
    void sendMessage_plus64Number_shouldUseSparkCarrier() {
        Message message = new Message("+64211234567", "Hello NZ", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals("Spark", result.getCarrier());
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void sendMessage_otherNumber_shouldUseGlobalCarrier() {
        Message message = new Message("+94772340562", "Hello SL", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals("Global", result.getCarrier());
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }
}
