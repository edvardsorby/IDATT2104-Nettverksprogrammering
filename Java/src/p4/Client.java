package src.p4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    boolean running = false;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }

    public void start() throws IOException {
        running = true;
        runClient();
    }

    private void runClient() throws IOException {

        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

        // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);


        System.out.println("Velkommen til klientprogrammet\n");
        while (running) {


            System.out.println("Vennligst oppgi første tall");

            int number1;
            while (true) {
                try {
                    number1 = Integer.parseInt(getInput());
                    buf = String.valueOf(number1).getBytes();
                    System.out.println(Arrays.toString(buf));
                    packet = new DatagramPacket(buf, buf.length, address, 4445);
                    socket.send(packet);
                    break;
                } catch (Exception e) {
                    System.out.println("Vennligst oppgi et gyldig tall");
                    e.printStackTrace();
                }
            }

            System.out.println("Vennligst oppgi andre tall");
            int number2;
            while (true) {
                try {
                    number2 = Integer.parseInt(getInput());
                    buf = String.valueOf(number2).getBytes();
                    System.out.println(Arrays.toString(buf));
                    packet = new DatagramPacket(buf, buf.length, address, 4445);
                    socket.send(packet);
                    break;
                } catch (Exception e) {
                    System.out.println("Vennligst oppgi et gyldig tall");
                }
            }

            System.out.println("Ønsker du addisjon (+) eller subtraksjon (-)?");
            char operator = 0;
            while (operator != '+' && operator != '-') {
                try {
                    operator = getInput().charAt(0);
                } catch (Exception e) {
                    System.out.println("Vennligst oppgi en gyldig operator");
                    e.printStackTrace();

                }
                System.out.println("Vennligst oppgi en gyldig operator");
            }
            buf = String.valueOf(operator).getBytes();
            System.out.println(Arrays.toString(buf));
            packet = new DatagramPacket(buf, buf.length, address, 4445);
            socket.send(packet);

            // get response
            buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Packet length: " + packet.getLength());
            System.out.println("received: " + received);
            int answer = Integer.parseInt(received);
            System.out.println(number1 + " " + operator + " " + number2 + " = " + answer + "\n");
        }

        /* Lukker forbindelsen */
        socket.close();
    }


    private String getInput() {
        Scanner in = new Scanner(System.in);

        return in.nextLine();
    }

    public void stop() {
        System.exit(0);
    }
}
