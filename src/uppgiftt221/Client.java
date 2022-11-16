package uppgiftt221;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable{

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;
    private Whiteboard whiteboard;
    //private Thread thread = new Thread(this);

    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        this.whiteboard = whiteboard;
    }

    public void sendThenReceive() {
        Whiteboard whiteboard = new Whiteboard(); //denna vill jag ska ta
        while(true) {
            try {
                GraphicsContext drawingToSend = whiteboard.getGraphicsContext();
                String graphicsContext = drawingToSend.toString();
                buffer = graphicsContext.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 2000);
                datagramSocket.send(datagramPacket);
                datagramSocket.receive(datagramPacket);

                String drawingFromServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(drawingFromServer);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void run() {
        while (true) {}
    }

    public void receivePoint(String messageAsPoint) {


    }

    public static void main(String[] args) throws Exception {
        //GUI WHITEBOARD:
        Application.launch(Whiteboard.class, args);

        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Client client = new Client(datagramSocket, inetAddress);
        System.out.println("Send datagram packet to a server");
        //client.sendThenReceive();
    }
}

