package src.p4;

import java.net.SocketException;

public class Server {
    public static void main(String[] args) throws SocketException {
        ServerThread thread = new ServerThread();
        thread.start();

    }
}
