package uppgift322;
//https://github.com/manaschaudhary/mail_receiver/blob/main/mailreceiver/src/main/java/com/rd/mailreceiver/EmailReceiver.java
/**
 * Using POP3 (Post Office Protocol) to receive Email-message
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailReceiver extends Application {

    private TextField emailServer;
    private TextField username;
    private TextField password;

    private String host;
    private String mailStoreType;
    private String usernameText;
    private String passwordText;

    public EmailReceiver() {
        this.host = "pop.gmail.com";
        this.mailStoreType = "pop3s";
        this.usernameText = username.getText();
        this.passwordText = password.getText();
    }

    /**
     * Launch application with GUI.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Receive Email");
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        root.setOrientation(Orientation.VERTICAL);

        Label label = new Label("Receive your emails");
        label.setAlignment(Pos.CENTER);

        //EMAIL SPECIFIC INFO:
        emailServer = new TextField();
        emailServer.setPromptText("Server name: ");
        if(!emailServer.getText().equalsIgnoreCase("pop3s") || !emailServer.getText().equalsIgnoreCase("imap")) {
            System.out.println("Error: Only server POP3S or IMAP can be used.");
        }
        username = new TextField();
        username.setPromptText("Username: ");
        password = new TextField();
        password.setPromptText("Password: ");

        TextArea viewMessage = new TextArea();


        Button receiveButton = new Button("Receive Emails");
        receiveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String message = checkEmails(emailServer.getText(), username.getText(), password.getText());
                    viewMessage.setText(message);
                    emailServer.clear(); username.clear(); password.clear();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        root.getChildren().addAll(label, emailServer, username, password);


        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Connects to server and creates a session, a pop3 object, a folder and prints the messages.
     * @param host
     * @param user
     * @param password
     */
    public static String checkEmails(String host, String user, String password) {
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host); //get host (pop.gmail.com for gmail)
            properties.put("mail.pop3.port", 995); //port number
            properties.put("mail.pop3.starttls.enable", true);

            Session emailSession = Session.getDefaultInstance(properties);

            //POP3 storeobject + connection w pop server:
            Store store = emailSession.getStore(); //can be 'Imap' as well
            store.connect(host, user, password);
            System.out.println("Connecting to server...");

            //folder object for open the inbox_
            Folder emailfolder = store.getFolder("INBOX");
            emailfolder.open(Folder.READ_ONLY);

            //Get messages:
            Message [] messages = emailfolder.getMessages(1, 6); //get all messages in inbox, choose how many by typing getMessages(1, 6) = get msg 1-6

            String totalMessages = "";
            for (int i=0, n=messages.length; i<n; i++) {
                Message message = messages[i];
                totalMessages = totalMessages +
                        "Message: " + (i+1) +
                        "\nFrom: " + message.getFrom()[0] +
                        "\nSubject: " + message.getSubject() + "\n";
            }
            //Close connection:
            emailfolder.close();
            store.close();
            return totalMessages;

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Runs application by applying with GUI and creates the EmailReceiver.
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    EmailReceiver receiver = new EmailReceiver();
                    launch(args);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
