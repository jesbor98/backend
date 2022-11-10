package uppgift212;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // response: listening for incoming connection for clients
    // and create a socket-object to communicate with them
    private ServerSocket serverSocket;
    private int port;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.port = 2000; //default port
    }

    public Server(ServerSocket serverSocket, int port) {
        this.serverSocket = serverSocket;
        this.port = port;
    }

    // startserver for keeping the server running:
    public void startServer() {

        // want our server to run until the serverSocket is closed
        try {
            while(!serverSocket.isClosed()) { //waiting for a client to connect while open
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientManager clientHandler = new ClientManager(socket); //implements Runnable

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {

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
        ServerSocket serverSocket = new ServerSocket(2000);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
