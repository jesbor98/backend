package uppgift213;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        // 3 ways to start the Client:
        String host = "127.0.0.1"; //default
        int port = 2000;
        if (args.length > 0) { //host + default port
            host = args[0];
        }
        if (args.length > 1) { //host + port
            port = Integer.parseInt(args[1]);
        }

        //Socket connecting to server
        Socket socket = new Socket(host, port);
        System.out.println("Connection with server established.");

        //Streams to read/write to server:
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        //Thread for receiving textmessages from chattserver
        //Thread for receiving text messages from chat server
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    try {
                        // Use SAXBuilder to validate and create a Document object
                        SAXBuilder saxBuilder = new SAXBuilder();
                        saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                        Document document = saxBuilder.build(new StringReader(message));

                        // Access the specific parts of the XML document to display to the user
                        Element header = document.getRootElement().getChild("header");
                        Element id = header.getChild("id");
                        String name = id.getChildText("name");
                        String email = id.getChildText("email");
                        String body = document.getRootElement().getChildText("body");

                        // Print out the message in a formatted way
                        System.out.println(name + " (" + email + "): " + body);
                    } catch (JDOMException e) {
                        // Print an error message if the message could not be parsed
                        System.out.println("KUNDE INTE PARSA ETT MEDDELANDE: " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //Read textmessages from users to then send to other clients connected to server
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            //Read textmessages from users to then send to other clients connected to server
            while (true) {
                //Read user input
                System.out.print("Your message: ");
                String message = userInput.readLine();
                if (message.equals("exit")) {
                    break;
                }

                //Create XML document
                Document document = new Document();
                Element root = new Element("message");
                document.setRootElement(root);

                //Create header element
                Element header = new Element("header");
                Element protocol = new Element("protocol");
                Element type = new Element("type");
                type.setText("text");
                Element version = new Element("version");
                version.setText("1.0");
                Element command = new Element("command");
                command.setText("send");
                protocol.addContent(type);
                protocol.addContent(version);
                protocol.addContent(command);
                Element id = new Element("id");
                Element name = new Element("name");
                name.setText("John Doe");
                Element email = new Element("email");
                email.setText("johndoe@example.com");
                Element homepage = new Element("homepage");
                homepage.setText("http://www.example.com");
                Element elementHost = new Element("host");
                elementHost.setText("127.0.0.1");
                id.addContent(name);
                id.addContent(email);
                id.addContent(homepage);
                id.addContent(elementHost);
                header.addContent(protocol);
                header.addContent(id);
                root.addContent(header);

                //Create body element
                Element body = new Element("body");
                body.setText(message);
                root.addContent(body);

                //Format XML document as string without line breaks
                XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
                String xmlString = outputter.outputString(document);

                //Send message to server
                out.println(xmlString);
                out.flush();
            }

        }
    }
}
