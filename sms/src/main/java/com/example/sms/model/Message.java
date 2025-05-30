package com.example.sms.model;

import java.util.UUID;

public class Message {
    private String id;
    private String destinationNumber;
    private String content;
    private String format;
    private String carrier;
    private MessageStatus status;

    public Message(String destinationNumber, String content, String format) {
        this.id = UUID.randomUUID().toString();
        this.destinationNumber = destinationNumber;
        this.content = content;
        this.format = format;
        this.status = MessageStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public String getContent() {
        return content;
    }

    public String getFormat() {
        return format;
    }

    public String getCarrier() {
        return carrier;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void setStatus(MessageStatus newStatus) {
        this.status = newStatus;
    }

}
