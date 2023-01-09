package uppgift211;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        // port enl uppgift:
        int port = 2000;
        /*if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }*/

        // server-socket + binda den till port:
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        // lista för att spara chattklienterna och dess sockets (f.a. flera clienter ska kunna ansluta till Servern):
        List<Socket> clientSockets = new ArrayList<>();

        // vänta på inkommande anslutningar + skapa en socket för varje anslutning
        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);
            new Thread(() -> {
                try {
                    // Skapa stream för att läsa + skriva data via socket:
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    // läs in textmeddelanden från chattklienten och skicka till alla andra chattklienter
                    while (true) {
                        String message = in.readLine();
                        if (message == null) {
                            break;
                        }
                        for (Socket socket : clientSockets) {
                            if (socket != clientSocket) {
                                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                out.println(message);
                                out.flush();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}