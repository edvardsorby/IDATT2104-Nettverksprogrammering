// Klienten kjøres med kommandoen 'node Client.js'

const net = require('net');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    WebSocket test page
    
    <input type="text" name="input" id="input">
    <button id="send">Send</button>
    <p id="output"></p>
    <script>
      var input = document.getElementById("input");
      var output = document.getElementById("output");
      var send = document.getElementById("send");
      
      
      
      let ws = new WebSocket('ws://localhost:3001');
      //let ws = new WebSocket('ws:10.22.212.14:3001');
      
      ws.onmessage = event => {
          output.innerHTML = event.data;
      }
      
      send.onclick = function () {
          ws.send(input.value);
          console.log("Sending message '" + input.value + "'");
      }
    </script>
  </body>
</html>
`;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});