package whiteboard221;


import javafx.application.Application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class WhiteboardServer extends Thread{

    Whiteboard whiteboard;
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private int host;
    private byte[] buffer;

    public WhiteboardServer(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(2000);
        WhiteboardServer server = new WhiteboardServer(datagramSocket);
        server.start();
        Application.launch(Whiteboard.class, args);
    }

    public void run() {
        while(true) {
            try {
                buffer = new byte[256];
                //New packet:
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket); //listen for packet, for connection + receive it

                //HÄR VILL JAG SAMLA PUNKTERNA SOM RITAS I EN STRING F.A. SEDAN
                //RITA UT PÅ SERVERNS WHITEBOARD??
                String point = "yo"; //skickar in decode här?
                buffer = point.getBytes(); //ta bytes f.a. lägga i bufferten

                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                datagramSocket.send(datagramPacket);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }


}
