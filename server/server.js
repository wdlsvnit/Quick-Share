var http = require('http');
var WebSocket = require('ws');
var uuid = require('node-uuid');

var clients = [];
const wsPort = 8080;
const httpPort = 8082;

var wss = new WebSocket.Server({
  port: wsPort
})

wss.on('connection', function connection(ws) {

  ws.on('message', function incomming(message) {
    console.log('Received: %s', message);

    var object = JSON.parse(message);
    if (object.type == 'new') {
      var uuidToken = uuid.v1();
      clients[uuid] = ws;
      console.log("Clients: " + clients);

      var msg = {
        type: 'new',
        uuid: uuidToken
      }
      ws.send(JSON.stringify(msg));
    }

  });
})

//create HTTP server
http.createServer(function (request, response) {

  if (request.method == 'POST') {
    var url = request.url;

    if (url == "/auth") {

      var body = '';

      request.on('data', function (data) {
        body = data;
      });

      request.on('end', function () {

        var params = JSON.parse(body);
        console.log("Recived Params: " + JSON.stringify(params));

        var uuId = params.uuid;
        var firebaseToken = params.firebase_token;

        var msg = {
          type: 'auth-done',
          firebaseToken: firebaseToken
        };

        console.log("Clients: " + clients.body);

        if (clients[uuId] != undefined || clients[uuId] != null) {
          console.log("Before " + Object.size(clients));
          clients[uuId].send(JSON.stringify(msg), {
            mask: false
          });
          delete clients[uuId];
          console.log("After " + Object.size(clients));

          response.end('{"status":"OK"}');
        } else {
          response.end('{"status":"NOK", "message":"WebSocket not found"}');
        }

      });
    } else {
      response.end('{"status":"NOK", "message":"Endpoint not found"}');

    }
  } else {
    response.end("Method not supported")
  }

}).listen(httpPort); //the server object listens on port httpPort