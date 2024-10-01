package src.p3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    boolean running = false;

    public void start() throws IOException {
        running = true;
        runClient();
    }

    private void runClient() throws IOException {

        final int PORTNR = 1250;
        Socket forbindelse = new Socket("localhost", PORTNR);

        /* Åpner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);


        System.out.println("Velkommen til klientprogrammet\n");
        while (running) {


            System.out.println("Vennligst oppgi første tall");

            int number1;
            while (true) {
                try {
                    number1 = Integer.parseInt(getInput());
                    skriveren.println(number1);
                    break;
                } catch (Exception e) {
                    System.out.println("Vennligst oppgi et gyldig tall");
                }
            }

            System.out.println("Vennligst oppgi andre tall");
            int number2;
            while (true) {
                try {
                    number2 = Integer.parseInt(getInput());
                    skriveren.println(number2);
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
                }
                System.out.println("Vennligst oppgi en gyldig operator");
            }
            skriveren.println(operator);

            int answer = Integer.parseInt(leseren.readLine());
            System.out.println(number1 + " " + operator + " " + number2 + " = " + answer + "\n");
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }


    private String getInput() {
        Scanner in = new Scanner(System.in);

        return in.nextLine();
    }

    public void stop() {
        System.exit(0);
    }
}
