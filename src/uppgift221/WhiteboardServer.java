package uppgift221;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardServer {
    private static final int PORT = 12345;

    private static List<Point> currentDrawing = new ArrayList<>();
    private static WhiteboardPanel whiteboard;

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            SwingUtilities.invokeLater(() -> {
                whiteboard = new WhiteboardPanel();
                createAndShowGUI();
            });

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] parts = receivedMessage.split(",");
                int x1 = Integer.parseInt(parts[0]);
                int y1 = Integer.parseInt(parts[1]);
                int x2 = Integer.parseInt(parts[2]);
                int y2 = Integer.parseInt(parts[3]);
                currentDrawing.add(new Point(x1, y1));
                currentDrawing.add(new Point(x2, y2));
                whiteboard.repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Whiteboard Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(whiteboard);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class WhiteboardPanel extends JPanel {
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
}
