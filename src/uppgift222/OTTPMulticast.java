package uppgift222;

import java.net.*;
import java.io.*;

public class OTTPMulticast {

    private static final String MULTICAST_ADDRESS = "234.235.236.237";
    private static final int PORT = 2000;
    private static final int MAX_MESSAGE_LENGTH = 1024;
    private static final int TIMEOUT_MS = 1000;

    private static boolean running = true;

    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket socket = new MulticastSocket(PORT);
            socket.setSoTimeout(TIMEOUT_MS);
            socket.joinGroup(group);

            String myName = "Alice"; // replace with your name
            String myHost = InetAddress.getLocalHost().getHostName();
            String myComment = "Hello, I'm online!"; // replace with your comment

            // Start a thread to send periodic OTTP messages
            new Thread(() -> {
                try {
                    while (running) {
                        String message = String.format("From: %s Host: %s Comment: %s", myName, myHost, myComment);
                        byte[] buf = message.getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
                        socket.send(packet);
                        Thread.sleep(1000); // send once per second
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            // Start a thread to receive and parse incoming OTTP messages
            new Thread(() -> {
                byte[] buf = new byte[MAX_MESSAGE_LENGTH];
                while (running) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(packet);
                        String message = new String(packet.getData(), 0, packet.getLength());
                        String[] parts = message.split(" ");
                        if (parts.length == 6 && parts[0].equals("From:") && parts[2].equals("Host:") && parts[4].equals("Comment:")) {
                            String name = parts[1];
                            String host = parts[3];
                            String comment = parts[5];
                            System.out.println(name + " --- " + host + " --- " + comment);
                        } else {
                            System.out.println("COULD NOT PARSE MESSAGE");
                        }
                    } catch (SocketTimeoutException e) {
                        // ignore and keep looping
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            System.out.println("Connected to " + group.getHostAddress() + ":" + PORT);
            System.out.println("Press Enter to quit.");
            System.in.read();
            running = false;
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

