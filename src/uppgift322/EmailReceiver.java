package uppgift322;
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

    /**
     * Creates GUI for application.
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
        emailServer.setPromptText("Server name: "); //using pop.gmail.com, could be Imap as well.
        username = new TextField();
        username.setPromptText("Username: ");
        password = new TextField();
        password.setPromptText("Password: ");



        Button receiveButton = new Button("Show Emails");
        receiveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String messages = checkEmails(emailServer.getText(), username.getText(), password.getText());
                    TextArea viewMessage = new TextArea();
                    viewMessage.setText(messages);
                    emailServer.clear(); username.clear(); password.clear();
                    root.getChildren().add(viewMessage);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        root.getChildren().addAll(label, emailServer, username, password, receiveButton);


        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Connects to email-inbox and parsing the text from emails to be shown for user.
     * @param host name
     * @param username for account (email-address)
     * @param password for account
     */
    public static String checkEmails(String host, String username, String password) {
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host); // using "pop.gmail.com" for gmail)
            properties.put("mail.pop3.port", 995); //port
            properties.put("mail.pop3.starttls.enable", true);

            Session emailSession = Session.getDefaultInstance(properties);

            //POP3 storeobject + connection with pop server:
            Store store = emailSession.getStore("pop3s"); //can be 'Imap' as well
            store.connect(host, username, password);
            System.out.println("Connecting to server...");

            //Folder object created to open the email-inbox:
            Folder emailfolder = store.getFolder("INBOX");
            emailfolder.open(Folder.READ_ONLY);

            Message [] messages = emailfolder.getMessages(1, 6); //without input, you show all messages in inbox. This prints out messages 1-6

            String emailMessages = "";
            for (int i=0, n=messages.length; i<n; i++) {
                Message message = messages[i];
                emailMessages = emailMessages +
                        "Message: " + (i+1) +
                        "\nFrom: " + message.getFrom()[0] +
                        "\nSubject: " + message.getSubject() + "\n";
            }
            //Close connection:
            //emailfolder.close();
            //store.close();
            return emailMessages;

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Runs application by launching GUI.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
