# Server written in Node.js

### Work Flow

#### Web
- On opening new tab in client(browser) it sends message of type `new` to server
- Server receives message and if of type `new` then creates `uuid` and sends it to client(browser)
- Web client shows QR code for received `uuid`

#### Android
- Login using Firebase and gets `firebase-id`
- Scans QR code and sends `POST` request to server using `/auth` endpoint and `uuid` , `firebase-id` parameters
- if WebSocket exists for `uuid` then sends `firebase-id` to respective WebSocket
