package com.example.sms.service;

import com.example.sms.model.Message;
import com.example.sms.model.MessageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

class MessageServiceTest {

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService();
    }

    @Test
    void testSendingMessageToValidNumber() {
        Message message = new Message("+61123456789", "Hello", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals(MessageStatus.DELIVERED, result.getStatus());
        assertNotNull(result.getCarrier());
    }

    @Test
    void testSendingMessageToInvalidNumber() {
        Message message = new Message("invalid-number", "Hello", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals(MessageStatus.FAILED, result.getStatus());
        assertNull(result.getCarrier());
    }

    @Test
    void testSendingMessageToOptedOutNumbers() {
        String optedOutNumber = "+61123456789";
        messageService.optOut(optedOutNumber);

        Message message = new Message(optedOutNumber, "Hello", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals(MessageStatus.BLOCKED, result.getStatus());
        assertNull(result.getCarrier());
    }

    @Test
    void testGetMessage() {
        Message message = new Message("+61123456789", "Hello", "SMS");
        messageService.sendMessage(message);

        Message retrieved = messageService.getMessage(message.getId());

        assertNotNull(retrieved);
        assertEquals("+61123456789", retrieved.getDestinationNumber());
        assertEquals(message, retrieved);
    }

    @Test
    void testRoutCarrierAlternativeForAu() {
        Message message1 = new Message("+61123456789", "Hello 1", "SMS");
        Message message2 = new Message("+61123456789", "Hello 2", "SMS");

        Message result1 = messageService.sendMessage(message1);
        Message result2 = messageService.sendMessage(message2);

        // Should alternate between Telstra and Optus
        assertNotEquals(result1.getCarrier(), result2.getCarrier());
    }

    @Test
    void testRoutCarrierForNz() {
        Message message = new Message("+64211234567", "Hello NZ", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals("Spark", result.getCarrier());
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void testCarrierForGlobal() {
        Message message = new Message("+94772340562", "Hello SL", "SMS");

        Message result = messageService.sendMessage(message);

        assertEquals("Global", result.getCarrier());
        assertEquals(MessageStatus.DELIVERED, result.getStatus());
    }

    @Test
    void testOptOutNumber() {
        String optedOutNumber = "+61123456780";
        messageService.optOut(optedOutNumber);
        Set<String> optedOutNumbers = messageService.getOptedOutNumbers();
        assertNotNull(optedOutNumbers);
        assertTrue(optedOutNumbers.contains(optedOutNumber));
    }

    @Test
    void testClearOptOutNumber() {
        String optedOutNumber = "+61123456780";
        messageService.optOut(optedOutNumber);
        messageService.clearOptedOutNumbers();
        Set<String> optedOutNumbers = messageService.getOptedOutNumbers();
        assertEquals(0, optedOutNumbers.size());

    }

    @Test
    void testClearMessageStore() {
        Message message1 = new Message("+94772340562", "Hello SL", "SMS");
        Message message2 = new Message("+64211234567", "Hello NZ", "SMS");
        messageService.sendMessage(message1);
        messageService.sendMessage(message2);

        Message sentMessage1 = messageService.getMessage(message1.getId());
        Message sentMessage2 = messageService.getMessage(message2.getId());
        assertNotNull(sentMessage1);
        assertNotNull(sentMessage2);
        messageService.clearMessageStore();
        assertNull(messageService.getMessage(message1.getId()));
        assertNull(messageService.getMessage(message2.getId()));

    }
}
