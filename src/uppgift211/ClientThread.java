package uppgift211;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable{

    private Thread clientThread = new Thread(this);
    private Socket socket;
    private BufferedReader inputReader;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        clientThread.start();
    }
    @Override
    public void run() {
        try {
            while(true) {
                String message = inputReader.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
