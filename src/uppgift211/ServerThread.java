package uppgift211;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread implements Runnable{
    private Thread serverThread = new Thread(this);
    private Socket client;
    private ArrayList<ServerThread> threads;
    private PrintWriter printWriter;


    public ServerThread(Socket client, ArrayList<ServerThread> threads) {
        this.client = client;
        this.threads = threads;
        serverThread.start();
    }

    /**
     * This method message back to all clients connected.
     * @param message that was sent from a client.
     */
    public void printMessages(String message) {
        for(ServerThread serverThread : threads) {
            serverThread.printWriter.println(client.getInetAddress().getHostName());
            serverThread.printWriter.println(message);
        }
    }

    /**
     * This method
     */
    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printWriter = new PrintWriter(client.getOutputStream(), true);

            while(true) {
                String message = input.readLine();
                printMessages(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
