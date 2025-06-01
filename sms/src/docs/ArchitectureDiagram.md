## 🏗️ Architecture Diagram

```text
                        ┌────────────────────────────┐
                        │      REST Clients          │
                        │ (Postman, cURL, Frontend)  │
                        └────────────┬───────────────┘
                                     │
                  ┌──────────────────▼──────────────────┐
                  │           Spring Boot App           │
                  └──────────────────┬──────────────────┘
                                     │
                             ┌───────▼─────────┐
                             │MessageController|
                             └─────────────────┘
                                     │
                ┌────────────────────▼─────────────────────────┐
                │               MessageService                 │
                │  - Send messages                             |
                |  - Gets messages                             │
                │  - Apply routing rules                       │
                │  - Track status: PENDING → SENT → DELIVERED  │
                │  - Check opt-out list                        │
                |  - Add to opt-out list                       |
                └───────────────────────┬──────────────────────┘
                                        │
                        ┌───────────────▼───────────────┐
                        │   In-Memory Storage           │
                        │  - Map<UUID, Message>         │
                        │  - Set<String> OptedOutNums   │
                        └───────────────────────────────┘

                         📶 Carrier Logic:
              ┌─────────────────────────────────┐
              │ +61 → Alternate Telstra/Optus   │
              │ +64 → Spark                     │
              │ Else → Global                   │
              └─────────────────────────────────┘
```

### 🤔 Assumptions

- Only AU (+61) and NZ (+64) are explicitly routed, other phone numbers' messages are routed as Global

- Opt-out list is in-memory and non-persistent

- Phone number format is assumed valid if it starts with a supported country code

- Carrier alternation for AU numbers resets on app restart

### ✅ Phone Number Validation

- Phone numbers with 10 to 15 digits

- Can optionally start with a + sign

- No letters or special characters allowed (besides the optional + at the beginning, whitespaces or parentheses)
