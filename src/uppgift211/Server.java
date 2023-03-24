package uppgift211;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 2000;

        //Connect to port
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        //More than one client can connect to server due to list
        List<Socket> clientSockets = new ArrayList<>();

        //Listnening on connections + create socket for every connection
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