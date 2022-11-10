package uppgift211;

import java.net.*;
import java.io.*;
import java.util.Scanner;

//https://www.youtube.com/watch?v=gchR3DpY-8Q&ab_channel=WittCode

public class Client {

//    private int port;
//    private String host;
//
//    public Client() {
//        this.port = 2000;
//        this.host = "127.0.0.1";
//    }
//
//    public Client(String host) {
//        this.port = 2000;
//        this.host = host;
//    }
//
//    public Client(String host, int port) {
//        this.port = port;
//        this.host = host;
//    }
//
//    public String getHost() {return host;}
//
//    public int getPort() {return port;}

    public static void main (String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader in = null;

        // bridge from bites -> characters
        OutputStreamWriter out = null;

        // Buffer = speed the input/output operations, read blocks at time
        // Wrap inputstream with buffer
        BufferedReader bf = null;
        BufferedWriter bw = null;

        try {
            socket = new Socket("127.0.0.1", 2000);

            in = new InputStreamReader(socket.getInputStream());
            out = new OutputStreamWriter(socket.getOutputStream());

            bf = new BufferedReader(in);
            bw = new BufferedWriter(out);

            Scanner scanner = new Scanner(System.in);

            while(true) {
                String msgToSend = scanner.nextLine();
                bw.write(msgToSend);
                bw.newLine();
                bw.flush(); //flushes the stream, usually used for really big texts, good for efficiency

                System.out.println("Server: " + bf.readLine());

                if(msgToSend.equalsIgnoreCase("BYE")) {
                    break; //out of the while-loop
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(socket != null)
                    socket.close();
                if(in != null)
                    in.close();
                if(out != null)
                    out.close();
                if(bf != null)
                    bf.close();
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
