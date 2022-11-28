package uppgift211;


import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * This class represents a Client connecting to a Server for then sending messages to the Server.
 *
 * @author Jessica Borg
 */


public class Client {

    private static final int DEFAULT_PORT = 2000;
    private static final String DEFAULT_HOST = "127.0.0.1";

    private int port;
    private String host;

    public Client() {
        this.port = DEFAULT_PORT;
        this.host = DEFAULT_HOST;
    }

    public Client(String host) {
        this.port = DEFAULT_PORT;
        this.host = host;
    }

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args) throws IOException {
        Socket connectionWithServer;
        BufferedReader bufferedReader;
        PrintWriter printWriter;
        Scanner scanner = new Scanner(System.in);

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        switch (args.length) {
            case 0:
                Client client = new Client(); break;
            case 1:
                host = args[0];
                client = new Client(host); break;
            case 2:
                host = args[0];
                port = Integer.parseInt(args[1]);
                client = new Client(host, port);
            default:
                break;
        }
        connectionWithServer = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(connectionWithServer.getInputStream()));
        printWriter = new PrintWriter(connectionWithServer.getOutputStream(), true);

        ClientThread clients = new ClientThread(connectionWithServer);
        String username = null;
        String messageBack;
        String input;

        System.out.println("Hello, welcome to the chat!");

        if (username == null) {
            System.out.println("Enter name to join the chat: ");
            input = scanner.nextLine();
            username = input;
        } else {
            String message = username + ": ";
            input = scanner.nextLine();
            System.out.println(message + input);

        }
    }







}

