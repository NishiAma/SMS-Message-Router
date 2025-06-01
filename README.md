# 📱 SMS Routing Service

A simple Spring Boot service for sending SMS messages with carrier-based routing, opt-out handling, and message status tracking.

---

## 🚀 Features

- ✉️ Send SMS via `POST /messages`
- 🔄 Retrieve message status via `GET /messages/{id}`
- ❌ Manage opt-outs via `POST /optout/{phoneNumber}`
- 📶 Route by phone number prefix to appropriate carrier
- ✅ Phone number validation

---

## 📁 Project Structure

```text
src/
├── main/java/com/example/sms/
│   ├── controller/                # REST controllers
│   ├── service/                   # Business logic
│   ├── model/                     # Request/response/data models
│   └── SmsRoutingServiceApplication.java
└── test/java/com/example/sms/     # Unit tests
```

## 💾 Storing data

For simplicity, this application stores the message data and opted out phone numbers in memory.

In `MessageService.java`, messages are being stored in a `ConcurrentHashMap`. The reason to pick a hashmap is so it can be easily accessed through the `id` of the mesage. The reason to use a concurrent hash map is that since this is a messaging service, it can be accessed with multiple clients and therefor there can be multiple attempts to access the data parallely. Therefore, it is thread-safe to use `ConcurrentHashMap`.

The opted-out phone numbers are being stored in a `Set` derived from a `ConcurrentHashMap`. The reason to use a `Set` is to avoid the same phone number being stored as an opted-out phone number over and over again. No matter how many times requested, one number is saved only onece. The reason to derive the `Set` from `ConcurrentHashMap` is to make it too, thread-safe. (This is an occation I took support from AI to decide the best data structure)

## ℹ️ More Information

The architecture diagram can be found in [ArchitectureDiagram.md](sms/src/docs/ArchitectureDiagram.md)

Information about API endpoints can be found in [APIEndpoints.md](sms/src/docs/APIEndpoints.md)
