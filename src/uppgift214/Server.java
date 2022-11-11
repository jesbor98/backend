package uppgift214;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {
    private BorderPane root = new BorderPane();

    @Override
    public void start(Stage stage) throws Exception {

        Label label = new Label("Waiting for image from client");
        root.setCenter(label);
        root.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(4848);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream(); //read info from client

        //Buffer the inputImage received:
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream); //image to display

        //bufferedInputStream.close();
        //socket.close();

        Label labelPicture = new Label();
        ImageView imageView = new ImageView(convertBufferedImage(bufferedImage)); //convert buffImage -> Image
        labelPicture.setGraphic(imageView);
        label.setText("Image received");
        root.setCenter(labelPicture);

        Label top = new Label("Server");
        top.setAlignment(Pos.CENTER);
        top.setPrefWidth(400);
        root.setTop(top);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public Image convertBufferedImage(BufferedImage bufferedImage) {
        WritableImage writableImage = new WritableImage(bufferedImage.getHeight(),
                bufferedImage.getWidth());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x<bufferedImage.getWidth(); x++) {
            for (int y = 0; y<bufferedImage.getHeight(); y++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }
        Image image = new ImageView(writableImage).getImage();
        return image;
    }

    public static void main(String[] args) {
        launch(args);
    }
}