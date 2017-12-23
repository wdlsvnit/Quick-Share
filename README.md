# Quick-Share
Project to share link(or text) quickly from one platform to another

## What is the idea?
Suppose you have website URL (like complex Google drive sharing URL)and you want to open on the desktop. You won't type whole URL character by character. Though you can use Facebook or social media web app but it will still need to log in and is clustered. The objective is to minimize time and effort to do this task.

## What is the plan?
The plan is to quickly share the link(or text) across platforms using the server, web app, and Android app. The project is divided into three parts,
1) Server
2) Web App
3) Android App

## Goal
To implement full duplex communication between platforms to share the link(text). The user will be able to copy from one platform and paste in another platform by sharing the clipboard. The project structure is like Whatsapp.

### Intial solution is by following
- User opens web app which will show QR code(Using WebSockets)
- User copies link(text) from mobile
- Popup will show(Like Google Tap to Translate) and on clicking it will open camera for scanning QR code
- After scanning, it will upload link(text) on database and will send it to Web Socket
