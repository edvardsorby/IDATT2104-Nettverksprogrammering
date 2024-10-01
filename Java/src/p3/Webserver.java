package src.p3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Webserver {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 80;

        ServerSocket tjener = new ServerSocket(PORTNR);

        Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt


        /* Åpner strømmer for kommunikasjon med klientprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);



        skriveren.println("HTTP/1.0 200 OK\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "\n" +
                "<HTML><BODY>\n" +
                "<H1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\n" +
                "<UL>");
        String linje = leseren.readLine();
        while (!linje.equals("")) {
            String headerLinje = leseren.readLine();
            skriveren.println("<LI>" + headerLinje + "</LI>");
            linje = leseren.readLine();
        }

        skriveren.println("</BODY></HTML>");

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
