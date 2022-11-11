package uppgift221;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
    private float x;
    private float y;

    //CLIENT OBJECT:
    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    @Override
    public void start(Stage stage) throws Exception {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        Client client = new Client(datagramSocket, inetAddress);
        System.out.println("Draw whatever you want!");
        client.send();

        Label top = new Label("The online Whiteboard!");
        top.setAlignment(Pos.CENTER);
        top.setPrefWidth(400);
        root.setTop(top);
        root.setVisible(true);


    }


    public void send() { //maybe implements Runnable instead since it always should run
        while(true) {

        }
    }

    class DrawHandler extends BorderPane implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {

        }
    }


}
