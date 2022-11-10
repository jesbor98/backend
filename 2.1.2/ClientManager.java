import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    //static arrrayList of every clientHandler object, static bc we want the list
    // to class not each object of the class
    // Keep track of all out clients:
    public static ArrayList<ClientManager> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader; // used to read data, msg that have been sent from the client
    private BufferedWriter bufferedWriter; // send data to a client, msg sent from other clients (using arraylist)
    private String clientUsername;

    public ClientManager (Socket socket) {
        try {
            // this is the object made from the class, for that object - set the
            // socket of it equal to what is passed to the constructor in Server-class
            this.socket = socket;
            // Bf & Bw from our socket : a connection between server, clienthandler, client
            // each socket: outputstream(send data)/inputstream(read data which has been sent to you)
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //Writer makes it to characters from bite
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine(); //typingtext from input
            clientHandlers.add(this); //add client to clientHandler

            broadcastMessage("SERVER: " + clientUsername + " has joined the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter); //close down everything
        }
    }

    //thread: listening for messages, cuz that is a "blocking operation"
    // means: the program will be stuck until the operation is completed
    // is no threads was used: program would be stuck waiting for a msg from a client
    // instead we have a separate thread waiting for msgs and another one working
    // with the rest of the application - otherwise our program would just wait for
    // msgs to come in
    @Override
    public void run() {
        // String to hold the msgs recieved from the client:
        String messageFromClient;
        while(socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine(); //hold here until msg is received
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break; //when client disconnect
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        //loop through ArrayList and sent message to all clients
        for(ClientManager clientHandler : clientHandlers) {
            // send msg to everyone except the one who sent it
            try { //send msg to client in username doesnt equals clientHandler
                if(!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend); //broadcast msg
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // With streams: you only need to close the outer wrapper as the underlying streams
    // are closed when u close the wrapper - only close bufferedReader+Writer
    // Closing socket = closing input/outputstream for that socket
}
