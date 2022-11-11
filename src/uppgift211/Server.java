package uppgift211;


import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

    public static void main(String [] args) throws IOException {

        Socket socket = null;
        InputStreamReader in = null;
        OutputStreamWriter out = null;
        BufferedReader bf = null;
        BufferedWriter bw = null;

        ServerSocket ss = null;

        ss = new ServerSocket(2001);

        while(true) {
            try {
                socket = ss.accept();
                System.out.println("Client connected");
                in = new InputStreamReader(socket.getInputStream());
                out = new OutputStreamWriter(socket.getOutputStream());

                bf = new BufferedReader(in);
                bw = new BufferedWriter(out);

                while(true) {
                    String msgFromClient = bf.readLine();
                    System.out.println("Client: " + msgFromClient);

                    bw.write("MSG Received");
                    bw.newLine();
                    bw.flush();

                    if(msgFromClient.equalsIgnoreCase("BYE")) {
                        break;
                    }
                }
                socket.close();
                in.close();
                out.close();
                bf.close();
                bw.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
