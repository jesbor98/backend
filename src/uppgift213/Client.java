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

        //Socket + anslut till server:
        Socket socket = new Socket(host, port);
        System.out.println("Connection with server established.");

        //In & out f.a. läsa/skriva till Server:
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        //Tar emot meddelanden från Server:
        new Thread(() -> {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {
                        break;
                    }
                    receiveXML(message);
                    //System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        //Läser in från användaren, med detta ska vi nu skicka msg som XML:
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String msg = userInput.readLine();
            if (msg.equalsIgnoreCase("exit")) {
                break;
            }
            sendXML(msg, host);
            out.println(msg);
            out.flush();
        }
    }

    static void sendXML(String msg, String host) {
        // Skapar ett nytt Document-objekt för att bygga upp ett meddelande
        Document message = new Document();

        // Skapar elementen för message, header, och body:
        Element messageElement = new Element("message");
        Element headerElement = new Element("header");
        Element bodyElement = new Element("body");

        // Lägger till elementen i message
        messageElement.addContent(headerElement);
        messageElement.addContent(bodyElement);

        // Skapar elementen för protocol och id
        Element protocolElement = new Element("protocol");
        Element idElement = new Element("id");

        // Lägger till protocol och id i header
        headerElement.addContent(protocolElement);
        headerElement.addContent(idElement);

        // Skapar elementen för type, version, command, name, email, homepage, och host
        Element typeElement = new Element("type");
        Element versionElement = new Element("version");
        Element commandElement = new Element("command");
        Element nameElement = new Element("name");
        Element emailElement = new Element("email");
        Element homepageElement = new Element("homepage");
        Element hostElement = new Element("host");

        // Lägger till type, version, command, name, email, homepage, och host i sin respektive förälder
        protocolElement.addContent(typeElement);
        protocolElement.addContent(versionElement);
        protocolElement.addContent(commandElement);
        idElement.addContent(nameElement);
        idElement.addContent(emailElement);
        idElement.addContent(homepageElement);
        idElement.addContent(hostElement);


        // Sätter innehållet för de olika elementen
        typeElement.setText("CTTP");
        versionElement.setText("1.0");
        commandElement.setText("MESS");
        nameElement.setText("Jessica Borg");
        emailElement.setText("jessicalborg@gmail.com");
        homepageElement.setText("https://www.example.com");
        hostElement.setText(host);
        bodyElement.setText(msg);

        // Lägger till message-elementet som rot i Document-objektet
        message.setRootElement(messageElement);

        // Formaterar och skriver ut XML-dokumentet
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(outputter.outputString(message));
    }


    static void receiveXML(String receivedXmlString) {
        // Skapar en ny SAXBuilder för att parsa mottagna meddelanden
        SAXBuilder builder = new SAXBuilder();

        // Kontrollerar validitet och skapar ett Document-objekt för det mottagna meddelandet
        Document receivedMessage;
        try {
            receivedMessage = builder.build(receivedXmlString);
            // Validerar mot DTD-filen
            builder.setFeature("http://xml.org/sax/features/validation", true);
            builder.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "https://atlas.dsv.su.se/~pierre/i/05_ass/ip1/2/2.1.3/message.dtd");
            //Använd detta Document-objekt för att accessa de specifika delarna i XML-dokumentet
            Element root = receivedMessage.getRootElement();
            Element header = root.getChild("header");
            Element protocol = header.getChild("protocol");
            Element id = header.getChild("id");
            Element name = id.getChild("name");
            Element email = id.getChild("email");
            Element body = root.getChild("body");
            System.out.println(name.getText() + "(" + email.getText() + "):" + body.getText());

        } catch (IOException | JDOMException e) {
            System.out.println("KUNDE INTE PARSA ETT MEDDELANDE:" + receivedXmlString);
        }
    }
}


