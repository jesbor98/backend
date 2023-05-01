package uppgift213;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 2000;

        // Connect to port
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        // More than one client can connect to server due to list
        List<Socket> clientSockets = new ArrayList<>();

        // Listnening on connections + create socket for every connection
        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);
            new Thread(() -> {
                try {
                    // Skapa stream för att läsa + skriva data via socket:
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    // läs in XML-meddelanden från chattklienten och skicka till alla andra chattklienter
                    while (true) {
                        String xmlString = in.readLine();
                        if (xmlString == null) {
                            break;
                        }

                        // Parsa XML-strängen till ett Document-objekt
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
                        doc.getDocumentElement().normalize();

                        // Hämta textinnehållet från meddelandet och skicka till alla andra klienter
                        String message = doc.getElementsByTagName("text").item(0).getTextContent();
                        for (Socket socket : clientSockets) {
                            if (socket != clientSocket) {
                                PrintWriter otherOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                otherOut.println(message);
                                otherOut.flush();
                            }
                        }
                    }
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
