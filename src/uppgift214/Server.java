package uppgift214;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.ObjectOutputStream;

public class Server {

    private static final int PORT = 4848;

    private List<ObjectOutputStream> outputStreams = new ArrayList<>();
    private JTextArea logArea;
    private JLabel imageLabel;

    public static int getPort() {
        return PORT;
    }

    public void start() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Server");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            imageLabel = new JLabel();
            frame.getContentPane().add(imageLabel, BorderLayout.SOUTH);
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


        // Lägg till följande metod i Server-klassen:
        private void displayImage(byte[] bytes) {
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                imageLabel.setIcon(new ImageIcon(image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void broadcast(byte[] bytes) {
            for (ObjectOutputStream stream : outputStreams) {
                try {
                    stream.writeObject(bytes);
                    stream.reset();
                    displayImage(bytes); // visa bilden på serverns skärm
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








