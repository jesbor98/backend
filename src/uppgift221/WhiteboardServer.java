package uppgift221;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


    public class WhiteboardServer {
        private static final int PORT = 5000;
        private DatagramSocket socket;
        private List<Point> points = new ArrayList<>();
        private List<InetAddress> clients = new ArrayList<>();

        public WhiteboardServer() {
            try {
                socket = new DatagramSocket(PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startListening();
        }

        private void startListening() {
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    try {
                        socket.receive(packet);
                        InetAddress clientAddress = packet.getAddress();
                        if (!clients.contains(clientAddress)) {
                            clients.add(clientAddress);
                        }
                        List<Point> receivedPoints = deserialize(packet.getData());
                        addPoints(receivedPoints);
                        sendPoints();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        private void addPoints(List<Point> points) {
            this.points.addAll(points);
        }

        private void sendPoints() {
            byte[] data = serialize(points);
            for (InetAddress clientAddress : clients) {
                DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, PORT);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            new WhiteboardServer();
        }
    }


