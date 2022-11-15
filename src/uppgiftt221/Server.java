package uppgiftt221;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import uppgift221.Point;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements Runnable{
    private DatagramSocket datagramSocket;
    private byte[] buffer;
   // private Thread thread = new Thread(this);
    private Whiteboard whiteboard;

    public Server(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        //thread.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();

                //Detta ska va punkter från det som ritats av client:
                String getData = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                // Det ska gå från string -> drawing:
                String[] point = getData.split(" ");
                int p1 = Integer.parseInt(point[0].trim());
                int p2 = Integer.parseInt(point[1].trim());

                Point p = new Point(p1, p2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void start(Stage s) {
        Pane pane = new Pane();
        Scene addFrame = new Scene(pane, 800, 500);
        s.setScene(addFrame);
        s.show();
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket();

        Application.launch(Whiteboard.class, args);
    }
}
