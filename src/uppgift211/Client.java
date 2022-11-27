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

    private Socket socket;
    private InputStreamReader in;
    private OutputStreamWriter out;
    private BufferedReader bf;
    private BufferedWriter bw;

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

    /*public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.sendMessage();
    }*/

    public static void main(String[] args) throws IOException {
        switch (args.length) {
            case 0:
                Client client0 = new Client();
                client0.sendMessage();
            case 1:
                String host = args[0];
                Client client1 = new Client(host);
                client1.sendMessage();
                break;
            case 2:
                String host2 = args[0];
                int port2 = Integer.parseInt(args[1]);
                Client client2 = new Client(host2, port2);
                client2.sendMessage();
            default:
                break;
        }
    }

    /**
     * This method returns the Client host.
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * This method returns the Client port.
     * @return port
     */
    public int getPort() {
        return port;
    }


    /**
     * This method sends a message to a Server by user input and receives a confirmation if the message was received and
     * what message was sent.
     *
     */
    public void sendMessage() {
        try {
            socket = new Socket(getHost(), getPort());

            in = new InputStreamReader(socket.getInputStream());
            out = new OutputStreamWriter(socket.getOutputStream());

            bf = new BufferedReader(in);
            bw = new BufferedWriter(out);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String msgToSend = scanner.nextLine();
                bw.write(msgToSend);
                bw.newLine();
                bw.flush(); //flushes the stream, usually used for really big texts, good for efficiency

                System.out.println("Server: " + bf.readLine());

                if (msgToSend.equalsIgnoreCase("BYE")) {
                    break; //out of the while-loop
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

