package src.p3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        ServerSocket tjener = new ServerSocket(PORTNR);

        while (true) {
            Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt
            System.out.println("Kontakt opprettet");

            ServerThread thread = new ServerThread(forbindelse);
            thread.start();
            System.out.println("Tråd startet");
        }
    }
}

class ServerThread extends Thread {

    Socket forbindelse;
    boolean running = false;

    public ServerThread(Socket forbindelse) {
        this.forbindelse = forbindelse;
    }

    public void run() {
        running = true;
        try {
            /* Åpner strømmer for kommunikasjon med klientprogrammet */
            InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
            BufferedReader leseren = new BufferedReader(leseforbindelse);
            PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


            while (running) {
                int tall1 = Integer.parseInt(leseren.readLine());  // mottar en linje med tekst
                int tall2 = Integer.parseInt(leseren.readLine());
                char operator = leseren.readLine().charAt(0);

                int resultat = calculate(tall1, tall2, operator);

                skriveren.println(resultat);
            }

            leseren.close();
            skriveren.close();
            forbindelse.close();
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
