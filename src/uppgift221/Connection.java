package uppgift221;


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//UDP class to handle datagram sockets, sending and receiving datagrams
class Connection {
    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;

    private String remoteHost;
    private int remotePort;

    private byte [] serializedMessage;
    private Whiteboard whiteboard;

    public Connection(int localPort, String remoteHost, int remotePort, Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
        try {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(localPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    /**
     * This method sends what has been drawn on the Whiteboard as a DatagramPacket by serializing it.
     * @param whiteboardMessage to send.
     * @throws IOException
     */
    public void sendIt(WhiteboardMessage whiteboardMessage) throws IOException {
        // Serialize to a byte array:
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(whiteboardMessage);
        oo.close();

        // Creates a byte-array to be sent as a DatagramPacket:
        serializedMessage = bStream.toByteArray();
        try {
            InetAddress remote = InetAddress.getByName(remoteHost); //send to this IP-adress
            sendSocket.send(new DatagramPacket(serializedMessage, serializedMessage.length, remote, remotePort)); //send to Ip address at port
            receiveSocket.receive(new DatagramPacket(serializedMessage, serializedMessage.length)); //receive the
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This function receives packet and converting it to a Point to be drawn on the canvas.
     * @return
     */
    public void receiveIt() {
        byte [] receiveBuffer = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        WhiteboardMessage whiteboardMessage = null;

        while(true) {
            try {
                sendSocket.receive(datagramPacket);
                byte[] newBuffer = new byte[datagramPacket.getLength()];

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(newBuffer));
                whiteboardMessage = (WhiteboardMessage) iStream.readObject();
                iStream.close();

                whiteboard.draw(whiteboardMessage.getPoint2D(), whiteboardMessage.getMouseEvent());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


}
