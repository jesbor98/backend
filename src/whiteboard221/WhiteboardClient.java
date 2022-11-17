package whiteboard221;

import javafx.application.Application;

import java.io.IOException;
import java.net.*;

public class WhiteboardClient {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private Whiteboard whiteboard;
    private byte[] buffer;

    public WhiteboardClient(DatagramSocket datagramSocket, InetAddress inetAddress, Whiteboard whiteboard) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        this.whiteboard = whiteboard;
    }

    public void connect() {
        try {
            //Create socket:
            DatagramSocket datagramSocket = new DatagramSocket();

            //Connect to server:
            byte[] buffer = new byte[256];
            InetAddress inetAddress = InetAddress.getByName("localhost");
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 2000);
            datagramSocket.send(datagramPacket);

            //Rebuild packet + request

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(Whiteboard.class, args);
    }


}
