package uppgift221;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class WhiteboardClient extends JFrame {
    private static final int PORT = 5000;
    private static final String HOST = "localhost";
    private DatagramSocket socket;
    private InetAddress address;
    private List<Point> points = new ArrayList<>();
    private JPanel drawPanel;

    public WhiteboardClient() {
        setTitle("Whiteboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(HOST);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                for (int i = 1; i < points.size(); i++) {
                    Point p1 = points.get(i - 1);
                    Point p2 = points.get(i);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        };
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addPoint(e.getPoint());
            }
        });
        drawPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                addPoint(e.getPoint());
                sendPoints();
            }
        });

        add(drawPanel, BorderLayout.CENTER);
        setVisible(true);
        startListening();
    }

    private void addPoint(Point p) {
        points.add(p);
        drawPanel.repaint();
    }

    private void sendPoints() {
        byte[] data = serialize(points);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListening() {
        new Thread(() -> {
            try {
                DatagramSocket serverSocket = new DatagramSocket();
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    serverSocket.receive(packet);
                    List<Point> receivedPoints = deserialize(packet.getData());
                    SwingUtilities.invokeLater(() -> addPoints(receivedPoints));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addPoints(List<Point> points) {
        this.points.addAll(points);
        drawPanel.repaint();
    }

    private byte[] serialize(List<Point> points) {
        try {
            return Serializer.serialize(points);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Point> deserialize(byte[] data) throws IOException, ClassNotFoundException {
        return (List<Point>) Serializer.deserialize(data);
    }

    private static class Serializer {
        public static byte[] serialize(Object obj) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            return out.toByteArray();
        }

        public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream objIn = new ObjectInputStream(in);
            return objIn.readObject();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WhiteboardClient());
    }
}


