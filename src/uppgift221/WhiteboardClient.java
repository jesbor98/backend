package uppgift221;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardClient {
    private static final int PORT = 12345;
    private static InetAddress serverAddress;
    private static DatagramSocket socket;

    private static List<Point> currentDrawing = new ArrayList<>();
    private static WhiteboardPanel whiteboard;

    public static void main(String[] args) {
        try {
            serverAddress = InetAddress.getByName("localhost"); // Anpassa IP-adressen för din server
            socket = new DatagramSocket();

            SwingUtilities.invokeLater(() -> {
                whiteboard = new WhiteboardPanel();
                createAndShowGUI();
            });

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Whiteboard Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(whiteboard);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class WhiteboardPanel extends JPanel {
        private Point startPoint;

        public WhiteboardPanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    Point endPoint = e.getPoint();
                    currentDrawing.add(startPoint);
                    currentDrawing.add(endPoint);
                    sendDrawingInstruction(startPoint, endPoint);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawCurrentDrawing(g);
        }

        private void drawCurrentDrawing(Graphics g) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < currentDrawing.size() - 1; i += 2) {
                Point p1 = currentDrawing.get(i);
                Point p2 = currentDrawing.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private static void sendDrawingInstruction(Point startPoint, Point endPoint) {
        try {
            String message = startPoint.x + "," + startPoint.y + "," + endPoint.x + "," + endPoint.y;
            byte[] sendData = message.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverAddress, PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



