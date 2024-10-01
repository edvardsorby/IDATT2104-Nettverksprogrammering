package src.p4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class ServerThread extends Thread {

    DatagramSocket socket = null;
    boolean running = false;

    public ServerThread() throws SocketException {
        this.socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;
        try {
            byte[] buf = new byte[256];

            // receive request
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet); // waits until a packet is received
            System.out.println("Initial packet received");

            InetAddress address = packet.getAddress();
            int port = packet.getPort();


            while (running) {
                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String tall1String = new String(packet.getData(), 0, packet.getLength());
                System.out.println(tall1String);
                int tall1 = Integer.parseInt(tall1String);
                System.out.println("Tall 1: " + tall1);

                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String tall2String = new String(packet.getData(), 0, packet.getLength());
                int tall2 = Integer.parseInt(tall2String);
                System.out.println("Tall 2: " + tall2);

                buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String operatorString = new String(packet.getData(), 0, packet.getLength());
                char operator = operatorString.charAt(0);
                System.out.println("Operator: " + operator);

                String resultat = String.valueOf(calculate(tall1, tall2, operator));
                System.out.println("Resultat: " + resultat);

                buf = resultat.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                System.out.println("Answer buf:" + Arrays.toString(buf));
                socket.send(packet);
                System.out.println("Packet length: " + packet.getLength());
            }

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int calculate(int number1, int number2, char operator) {
        return switch (operator) {
            case '+' -> number1 + number2;
            case '-' -> number1 - number2;
            default -> 0;
        };

    }
}
