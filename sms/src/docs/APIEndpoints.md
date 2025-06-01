## API Endpoints

### ✉️ Send Message

🔗 API endpoint: `POST /messages`
📥 Example Request Body:

```
{
  "destination_number": "+61412345678",
  "content": "Hello world",
  "format": "SMS"
}
```

📤 Example Response:

```
{
  "id": "e7c5e6e2-b17b-4e8e-91ae-61cfd7b8325a",
  "status": "DELIVERED"
}
```

### 📊 Get Message Status

🔗 API endpoint: `GET /messages/{id}`
📤 Example Response:

```
{
  "id": "e7c5e6e2-b17b-4e8e-91ae-61cfd7b8325a",
  "status": "DELIVERED",
}
```

### 🚫 Opt-Out Phone Number

🔗 API endpoint: `POST /optout/{phoneNumber}`

📤 Example Response:

```
{
    "message": "Opt-out successful for +6123456789"
}
```
