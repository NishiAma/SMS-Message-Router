package com.example.sms.controller;

import com.example.sms.model.Message;
import com.example.sms.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        messageService.clearOptedOutNumbers();
        messageService.clearMessageStore();
    }

    @Test
    public void testSendValidAUNumber() throws Exception {
        String json = """
                {
                    "destinationNumber": "+61491570156",
                    "content": "Hello world",
                    "format": "SMS"
                }""";
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DELIVERED")));
    }

    @Test
    public void testSendInvalidAUNumber() throws Exception {
        String json = """
                {
                    "destinationNumber": "invalid-number",
                    "content": "Hello world",
                    "format": "SMS"
                }""";
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("FAILED")));
    }

    @Test
    public void testSendToOptedOutNumber() throws Exception {
        String number = "+61491570156";
        messageService.optOut(number);

        String json = """
                {
                    "destinationNumber": "%s",
                    "content": "Blocked message",
                    "format": "SMS"
                }""".formatted(number);
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("BLOCKED")));
    }

    @Test
    public void testSendToNZNumber() throws Exception {
        String json = """
                {
                    "destinationNumber": "+64211234567",
                    "content": "Hello NZ",
                    "format": "SMS"
                }""";
        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DELIVERED")));
    }

    @Test
    public void testGetMessagesFound() throws Exception {
        Message message = new Message("+61491570156", "Hello test", "SMS");
        messageService.sendMessage(message);
        mockMvc.perform(get("/messages/" + message.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(message.getId())))
                .andExpect(jsonPath("$.status", is("DELIVERED")));
    }

    @Test
    public void testGetMessagesNotFound() throws Exception {
        mockMvc.perform(get("/messages/nonexistent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testOptOut() throws Exception {
        String phoneNumber = "+61491570156";

        mockMvc.perform(post("/optout/" + phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Opt-out successful for " + phoneNumber)));

        assertTrue(messageService.getOptedOutNumbers().contains(phoneNumber));
    }

    @Test
    public void testOptOutIdempotent() throws Exception {
        String phoneNumber = "+61491570156";

        mockMvc.perform(post("/optout/" + phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Opt-out successful for " + phoneNumber)));

        mockMvc.perform(post("/optout/" + phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Opt-out successful for " + phoneNumber)));

        assertTrue(messageService.getOptedOutNumbers().contains(phoneNumber));
        assertEquals(1, messageService.getOptedOutNumbers().size());
    }

}
