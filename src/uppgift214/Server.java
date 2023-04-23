package uppgift214;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server extends JFrame {

    private JLabel imageLabel;

    public Server() {
        super("Image Receiver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        imageLabel = new JLabel();
        add(imageLabel);
        setVisible(true);
    }

    public void receiveImage(int port) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        while (true) {
            BufferedImage image = ImageIO.read(in);
            imageLabel.setIcon(new ImageIcon(image));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server receiver = new Server();
        receiver.receiveImage(8000);
    }
}








