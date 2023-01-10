package uppgift212;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // response: listening for incoming connection for clients
    // and create a socket-object to communicate with them
    private ServerSocket serverSocket;
   // private int port;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // startserver for keeping the server running:
    public void start() {
        int nrOfClientsConnected = 0;
        // want our server to run until the serverSocket is closed
        try {
            while(!serverSocket.isClosed()) { //waiting for a client to connect while open
                Socket socket = serverSocket.accept();
                System.out.println("Server running on: \n" +
                        " HOST: " + serverSocket.getInetAddress().getLocalHost().getHostName() + "\n" + "PORT: " + serverSocket.getLocalPort() );
                nrOfClientsConnected++;
                System.out.println("Connected clients: " + nrOfClientsConnected);
                ClientManager clientManager = new ClientManager(socket); //implements Runnable

                Thread thread = new Thread(clientManager);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 2000;
        if (args.length > 0) { //host + default port
            port = Integer.parseInt(args[0]);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket);
        server.start();
    }
}
