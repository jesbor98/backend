package uppgift221;

import javafx.scene.shape.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardProgram {
    private static int PORT = 2000; // Default port for communication
    private static InetAddress serverAddress; // Address of the other instance
    private static List<Point> currentPath = new ArrayList<>();
    private static List<List<Point>> paths = new ArrayList<>();
    private static JPanel whiteboard;

    private static void startServer() {
        new Thread(() -> {
            try {
                DatagramSocket serverSocket = new DatagramSocket(PORT);
                byte[] receiveData = new byte[1024];

                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    String[] parts = receivedData.split(",");

                    if (parts[0].equals("path")) {
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        currentPath.add(new Point(x, y));
                    }

                    SwingUtilities.invokeLater(() -> whiteboard.repaint());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private static void startClient(int serverPort) {
        JFrame frame = new JFrame("Draw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        whiteboard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (List<Point> path : paths) {
                    for (int i = 1; i < path.size(); i++) {
                        Point prevPoint = path.get(i - 1);
                        Point currPoint = path.get(i);
                        g.drawLine(prevPoint.x, prevPoint.y, currPoint.x, currPoint.y);
                    }
                }

                // Draw the current path while dragging
                for (int i = 1; i < currentPath.size(); i++) {
                    Point prevPoint = currentPath.get(i - 1);
                    Point currPoint = currentPath.get(i);
                    g.drawLine(prevPoint.x, prevPoint.y, currPoint.x, currPoint.y);
                }
            }
        };

        whiteboard.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentPath.clear(); // Clear the previous path
                currentPath.add(e.getPoint()); // Add the starting point to the path
            }
        });

        whiteboard.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                currentPath.add(new Point(x, y));

                // Send the point to the other instances
                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    String data = "path," + x + "," + y;
                    byte[] sendData = data.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                    clientSocket.send(sendPacket);
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                whiteboard.repaint();
            }
        });


        whiteboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!currentPath.isEmpty()) {
                    paths.add(new ArrayList<>(currentPath)); // Add the completed path
                    currentPath.clear(); // Clear the current path

                    // Send the completed path to the server
                    try {
                        DatagramSocket clientSocket = new DatagramSocket();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(paths.get(paths.size() - 1));
                        oos.close();
                        byte[] sendData = baos.toByteArray();

                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                        clientSocket.send(sendPacket);
                        clientSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    whiteboard.repaint();
                }
            }
        });

        frame.add(whiteboard);
        frame.setVisible(true);

        // Set the serverAddress based on the argument passed to the method
        try {
            serverAddress = InetAddress.getByName("localhost"); // Change to your server address
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws UnknownHostException {
        if (args.length >= 3) {
            PORT = Integer.parseInt(args[0]);
            serverAddress = InetAddress.getByName(args[1]);
            int serverPort = Integer.parseInt(args[2]);

            if (args.length >= 4 && args[3].equals("server")) {
                // Start as server and connect as client
                startServer();
                startClient(serverPort);
            } else {
                // Start as client and connect as server
                startClient(serverPort);
                startServer();
            }
        } else {
            startServer();
            startClient(PORT);
        }
    }

    //Testa denna sen när du kör via command-line också
    /*public static void main(String[] args) {
        if (args.length == 1) {
            int port = Integer.parseInt(args[0]);
            startServer(port);
        } else if (args.length == 3) {
            int serverPort = Integer.parseInt(args[0]);
            String serverAddress = args[1];
            int clientPort = Integer.parseInt(args[2]);
            startClient(serverAddress, serverPort, clientPort);
        } else {
            System.out.println("Usage: java Draw <serverPort>");
            System.out.println("       java Draw <serverPort> <serverAddress> <clientPort>");
        }
    }
    */
}

