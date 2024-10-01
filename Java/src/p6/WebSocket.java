package src.p6;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocket {
    private static final ArrayList<ClientThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        try (ServerSocket server = new ServerSocket(3001)) { // localhost
        //try (ServerSocket server = new ServerSocket(3001, 0, InetAddress.getByName("10.22.212.14"))) { // local network

            System.out.println("Server has started on port 3001.\r\nWaiting for a connection…");

            while (true) {
                Socket client = server.accept(); // Waiting for a client to connect
                System.out.println("A client connected.");

                ClientThread clientThread = new ClientThread(client); // Create a new thread for each client
                clients.add(clientThread);
                clientThread.start();
            }
        }
    }

    private static class ClientThread extends Thread {
        private final Socket client;
        private final InputStream in;
        private final OutputStream out;

        public ClientThread(Socket client) throws IOException {
            this.client = client;
            this.in = client.getInputStream();
            this.out = client.getOutputStream();
        }

        @Override
        public void run() {
            try {
                Scanner s = new Scanner(in, StandardCharsets.UTF_8);

                String data = s.useDelimiter("\\r\\n\\r\\n").next();
                Matcher get = Pattern.compile("^GET").matcher(data);

                if (get.find()) {

                    // The handshaking process
                    Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                    match.find();

                    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Sec-WebSocket-Accept: "
                            + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1")
                            .digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                    .getBytes("UTF-8")))
                            + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
                    out.write(response, 0, response.length);

                    // Start the WebSocket communication
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = in.read(buffer);
                        if (len == -1) {
                            break;
                        }

                        String message = decodeMessage(in);
                        System.out.println("Received message: " + message);

                        // Send the message to all connected clients
                        for (ClientThread clientThread : clients) {
                            clientThread.out.write(Objects.requireNonNull(encodeMessage(message)));
                        }
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    client.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public static String decodeMessage(InputStream in) throws IOException {
            byte[] buffer = new byte[1024];
            int len = in.read(buffer, 0, 6);
            if (len == -1) {
                return null;
            }
            byte[] encoded = new byte[len];
            System.arraycopy(buffer, 0, encoded, 0, len);
            byte[] key = new byte[]{buffer[2], buffer[3], buffer[4], buffer[5]};
            int payloadLength = (buffer[1] & 0x7F);
            if (payloadLength == 126) {
                len = in.read(buffer, 0, 2);
                if (len == -1) {
                    return null;
                }
                payloadLength = ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
            } else if (payloadLength == 127) {
                len = in.read(buffer, 0, 8);
                if (len == -1) {
                    return null;
                }
                payloadLength = ((buffer[0] & 0xFF) << 56) |
                        ((buffer[1] & 0xFF) << 48) |
                        ((buffer[2] & 0xFF) << 40) |
                        ((buffer[3] & 0xFF) << 32) |
                        ((buffer[4] & 0xFF) << 24) |
                        ((buffer[5] & 0xFF) << 16) |
                        ((buffer[6] & 0xFF) << 8) |
                        ((buffer[7] & 0xFF));
            }
            byte[] decoded = new byte[payloadLength];
            for (int i = 0; i < payloadLength; i++) {
                decoded[i] = (byte) (in.read() ^ key[i % 4]);
            }
            return new String(decoded, StandardCharsets.UTF_8);
        }
    }

    private static byte[] encodeMessage(String message) {
            try {
                byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
                byte[] encoded = new byte[messageBytes.length + 2];
                encoded[0] = (byte) 0x81;
                encoded[1] = (byte) messageBytes.length;

                System.arraycopy(messageBytes, 0, encoded, 2, messageBytes.length);

                return encoded;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
}
