package uppgift214;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {

    private static final int PORT = 4848;

    private List<ObjectOutputStream> outputStreams = new ArrayList<>();
    private JTextArea logArea;

    public static int getPort() {
        return PORT;
    }

    public void start() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Server");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            logArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(logArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            frame.pack();
            frame.setVisible(true);
        });

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            log("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                log("New client connected: " + clientSocket.getInetAddress());

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStreams.add(out);

                ClientHandler clientHandler = new ClientHandler(clientSocket, out);
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket, ObjectOutputStream out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    in = new ObjectInputStream(socket.getInputStream());
                    Object obj = in.readObject();
                    if (obj instanceof byte[]) {
                        byte[] bytes = (byte[]) obj;
                        log("Received image from client: " + bytes.length + " bytes");
                        broadcast(bytes);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                    outputStreams.remove(out);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(byte[] bytes) {
            for (ObjectOutputStream stream : outputStreams) {
                try {
                    stream.writeObject(bytes);
                    stream.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void log(String message) {
        EventQueue.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}

/*public class Server extends JFrame implements Serializable {

    /*private JLabel imageLabel;

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
        receiver.receiveImage(5000);
    }
}*/









