package uppgift211;
/**
 * This class act as a Server which a Client connects to by a Stream Socket connection. When connected the class
 * receives messages sent from the Client and confirms back to the Client what message was sent.
 *
 * @author Jessica Borg
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

    private Socket socket;
    private InputStreamReader in;
    private OutputStreamWriter out;
    private BufferedReader bf;
    private BufferedWriter bw;

    private ServerSocket ss;
    private int port;

    public Server() {
        port = 2000;
    }
    public Server(int port) {
        this.port = port;
    }


    /**
     * This method creates a connection with a Client that wants to connect to the Server.
     * If connection succeeds the Server waits for message from Client and displaying them back.
     * @throws IOException
     */
    public void receiveMessageAndConfirm() throws IOException {
        ss = new ServerSocket(2000);
        while(true) {
            try {
                socket = ss.accept();
                System.out.println("Client connected");
                in = new InputStreamReader(socket.getInputStream());
                out = new OutputStreamWriter(socket.getOutputStream());

                bf = new BufferedReader(in);
                bw = new BufferedWriter(out);

                while(true) {
                    String msgFromClient = bf.readLine();
                    System.out.println("Client: " + msgFromClient);

                    bw.write("MSG Received: " + msgFromClient);
                    bw.newLine();
                    bw.flush();

                    if(msgFromClient.equalsIgnoreCase("BYE")) {
                        break;
                    }
                }
                socket.close(); in.close(); out.close(); bf.close(); bw.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

   public static void main(String[] args) throws IOException {
        switch(args.length) {
            case 0:
                Server server = new Server();
                server.receiveMessageAndConfirm();
            case 1:
                Server server1 = new Server();
                server1.receiveMessageAndConfirm();
            case 2:
                int port = Integer.parseInt(args[2]);
                Server server2 = new Server(port);
                server2.receiveMessageAndConfirm();

            default:
                break;
        }
    }
}