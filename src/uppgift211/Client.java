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

    private Socket socket;
    private InputStreamReader in;
    private OutputStreamWriter out;
    private BufferedReader bf;
    private BufferedWriter bw;

    private int port;
    private String host;

    public Client() {
        this.port = 2000;
        this.host = "127.0.0.1";
    }

    public Client(String host) {
        this.port = 2000;
        this.host = host;
    }

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    /**
     * This method returns the Client host.
     * @return host
     */
    public String getHost() {return host;}

    /**
     * This method returns the Client port.
     * @return port
     */
    public int getPort() {return port;}


    /**
     * This method sends a message to a Server by user input and receives a confirmation if the message was received and
     * what message was sent.
     * @throws IOException
     */
    public void sendMessage() throws IOException {
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

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.sendMessage();
    }
}

