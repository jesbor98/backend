package uppgiftt221;

import javafx.application.Application;
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

    public Client(DatagramSocket datagramSocket, InetAddress inetAddress, Whiteboard whiteboard) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
        this.whiteboard = whiteboard;
    }

    public void sendPoint(Point p) {
        String point = Integer.toString(p.x) + " " + Integer.toString(p.y);
        buffer = point.getBytes(StandardCharsets.UTF_8); //ha detta?
       /* try {
           // InetAddress inetAddress = InetAddress.getByName(host);
            //DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
           // datagramSocket.send(datagramPacket);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        //Client client = new Client(datagramSocket, inetAddress);
        System.out.println("Send datagram packet to a server");
        //client.sendThenReceive();
    }
}

