package uppgift221;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;

public class Client extends Application {

    //INPUT/OUTPUT:
    private InputStreamReader in = null;
    private OutputStreamWriter out = null;
    private BufferedReader bf = null;
    private BufferedWriter bw = null;

    //DATA TRANSFER:
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;

    //GUI:
    private BorderPane root = new BorderPane();
    private Button button;

    //COORDINATES FOR DRAWING:
    private Point point;
    private double x, y;

    //CLIENT OBJECT:
    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    @Override
    public void start(Stage stage) throws Exception {

        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        System.out.println("Draw whatever you want!");
        //Client client = new Client(datagramSocket, inetAddress);

        Label top = new Label("The online Whiteboard!");
        top.setAlignment(Pos.CENTER);
        top.setPrefWidth(400);
        root.setTop(top);
        root.setVisible(true);

        //While true:
        root.setOnMousePressed(new MouseStart());
        root.setOnMouseDragged(new MouseDragged());

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }

    class MouseStart implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            x = mouseEvent.getX();
            y = mouseEvent.getY();
        }
    }

    class MouseDragged implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double newX = root.getLayoutX() + mouseEvent.getX() - x;
            double newY = root.getLayoutY() + mouseEvent.getY() - y;
            root.relocate(newX, newY);
        }
    }

    public void receivePoint(String message) {
        HashSet<Point> set = new HashSet<>();
        String[] xy = message.split(" ");
        for(int i = 0 ; i<message.length(); i++) {
            Point p = new Point(Integer.parseInt(xy[i]), Integer.parseInt(xy[i+1]));
            set.add(p);
        }
    }

    public void sendThenReceive() {
        //Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                String messageToSend = pointToString(point);
                buffer = messageToSend.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 2000);
                datagramSocket.send(datagramPacket);
                datagramSocket.receive(datagramPacket);

                String messageFromServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(messageFromServer);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    public String pointToString(Point p) {
        String point = (int) p.getX() + " " + ((int) p.getY());
        System.out.println(point);
        return point;
    }

    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
    }

    public static void main(String[] args) {

        launch(args);
    }

}

