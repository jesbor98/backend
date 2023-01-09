package uppgift211;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        // 3 ways to start the Client:
        String host = "127.0.0.1"; //default
        int port = 2000;
        if (args.length > 0) { //host + default port
            host = args[0];
        }
        if (args.length > 1) { //host + port
            port = Integer.parseInt(args[1]);
        }

        //Socket + anslut till server:
        Socket socket = new Socket(host, port);
        System.out.println("Connection with server established.");

        // Streams f.a. läsa/skriva till servern:
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Skapa en tråd för att ta emot textmeddelanden från chattservern
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //loop fa läsa in textmeddelanden från användaren och skicka till chattservern
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String message = userInput.readLine();
            if (message.equals("quit")) {
                break;
            }
            out.println(message);
            out.flush();
        }

        // Stäng socketen + streamerna
        socket.close();
        in.close();
        out.close();
    }
}
