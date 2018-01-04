var WebSocket = require('ws');
var ws = new WebSocket('ws://localhost:8080');

ws.on('open', function open() {

    //Send 'new' message code to server.
    var code = {
        type: 'new'
    };
    ws.send(JSON.stringify(code));
});

ws.on('message', function (data, flags) {
    var obj = JSON.parse(data);

    if (obj.type == 'new') {
        console.log("Receivied uuid from server: " + JSON.stringify(obj));
    } else if (obj.type = 'auth-done') {
        console.log("Got firebase token " + obj.firebaseToken);
    }
});