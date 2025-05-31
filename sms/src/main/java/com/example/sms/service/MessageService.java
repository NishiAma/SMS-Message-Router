package com.example.sms.service;

import com.example.sms.model.Message;
import com.example.sms.model.MessageStatus;
import com.example.sms.util.PhoneNumberUtils;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageService {
    private final Map<String, Message> messageStore = new ConcurrentHashMap<>();
    private final Set<String> optedOutNumbers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private boolean alternateCarrier = false;

    public Message sendMessage(Message message) {
        String phoneNumber = message.getDestinationNumber();
        if (optedOutNumbers.contains(phoneNumber)) {
            message.setStatus(MessageStatus.BLOCKED);
        } else if (PhoneNumberUtils.isValidPhoneNumber(phoneNumber)) {
            alternateCarrier = !alternateCarrier;
            String carrier = PhoneNumberUtils.routeCarrier(phoneNumber, alternateCarrier);
            message.setCarrier(carrier);
            message.setStatus(MessageStatus.SENT);
            message.setStatus(MessageStatus.DELIVERED);
        } else {
            message.setStatus(MessageStatus.FAILED);
        }
        messageStore.put(message.getId(), message);
        return message;
    }

    public Message getMessage(String id) {
        return messageStore.get(id);
    }

    public void optOut(String phoneNumber) {
        optedOutNumbers.add(phoneNumber);
    }

    public Set<String> getOptedOutNumbers() {
        return optedOutNumbers;
    }

    public void clearOptedOutNumbers() {
        optedOutNumbers.clear();
    }

    public void clearMessageStore() {
        messageStore.clear();
    }
}
