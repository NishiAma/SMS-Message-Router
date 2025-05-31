package com.example.sms.controller;

import com.example.sms.model.Message;
import com.example.sms.service.MessageService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        Message savedMessage = messageService.sendMessage(message);
        return ResponseEntity.ok(Map.of("id", savedMessage.getId(), "status", savedMessage.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageStatus(@PathVariable String id) {
        Message message = messageService.getMessage(id);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("id", message.getId(), "status", message.getStatus()));
    }

    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<?> optOut(@PathVariable String phoneNumber) {
        messageService.optOut(phoneNumber);
        return ResponseEntity.ok(Map.of("message", "Opt-out successful for " + phoneNumber));
    }
}
