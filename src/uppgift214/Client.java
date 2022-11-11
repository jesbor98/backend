package uppgift214;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client extends Application {
    private BorderPane root = new BorderPane();
    private Button button;

    @Override
    public void start(Stage stage) throws Exception {
        Socket socket = new Socket("atlas.dsv.su.se", 4848);
        System.out.println("Connected to server");

        Label top = new Label("Client");
        top.setAlignment(Pos.CENTER);
        top.setPrefWidth(400);
        root.setTop(top);
        root.setVisible(true);

        button = new Button("Send image to server");
        button.setAlignment(Pos.CENTER);
        button.setLayoutX(800);
        button.setLayoutY(100);

        Image image = new Image("file:C:/Users/jessi/bart.png"); //url till bild
        ImageView imageView = new ImageView(image);
        root.setCenter(imageView);
        //root.getChildren().add(imageView);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) { // Här skickas bilden
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream); //wrap,
                    Image image = imageView.getImage();
                    BufferedImage bufferedImage = new BufferedImage((int)image.getWidth(), (int)image.getHeight(), BufferedImage.TYPE_INT_RGB);

                    Graphics graphics = bufferedImage.createGraphics();
                    graphics.drawImage(bufferedImage, 0 ,0, null);
                    graphics.dispose();

                    ImageIO.write(bufferedImage, "png", bufferedOutputStream);

                    //bufferedOutputStream.close();
                    //socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        root.setBottom(button);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
