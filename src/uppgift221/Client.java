package uppgift221;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;

    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    public void send() {

    }

    public void receive() {

    }


}
