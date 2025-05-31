package com.example.sms.controller;

import com.example.sms.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
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
    public void testGetMessages() throws Exception {
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

}
