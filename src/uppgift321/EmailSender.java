package uppgift321;

/**
 * Using SMTP (Simple Mail Transfer Protocol) to send Email.
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

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender extends Application {

    private static final String GMAIL_HOST = "smtp.gmail.com";
    private static final int GMAIL_PORT = 587;

    private Session session;
    private TextField username;
    private TextField password;
    private TextField from;
    private TextField to;
    private TextField subject;
    private TextArea messageText;

    /**
     * Launch application and GUI.
     * When user presses the Send-Button - email will send.
     * @param stage where GUI appears.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Send Email");
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        root.setOrientation(Orientation.VERTICAL);

        Label labelSend = new Label("Send email: ");
        labelSend.setAlignment(Pos.CENTER);

        Label labelAuth = new Label("Enter username and password for gmail-account: ");

        // Account specific information:
        username = new TextField();
        username.setPromptText("Username: ");
        password = new TextField();
        password.setPromptText("Password: ");

        // Email specific information:
        from = new TextField();
        from.setPromptText("From: ");
        to = new TextField();
        to.setPromptText("To: ");
        subject = new TextField();
        subject.setPromptText("Subject: ");

        messageText = new TextArea();
        messageText.setPromptText("Message: ");

        getProperties();

        Button sendButton = new Button("Send");
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    sendEmail();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
        root.getChildren().addAll(labelAuth, username, password, labelSend, from, to, subject, messageText, sendButton);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Apply properties to send email.
     */
    public void getProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", true); //gmail
        properties.put("mail.smtp.host", GMAIL_HOST); //connect to gmail
        properties.put("mail.smtp.port", GMAIL_PORT); //port 587 for gmail (465/default 25)
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.transport.protocol", "smtp");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username.getText(), password.getText());
            }
        });
    }

    /**
     * Send email with MimeMessage class and prepping the email to send.
     * @throws MessagingException
     */
    public void sendEmail() throws MessagingException {
        MimeMessage message = prepMessage(session, from.getText(), to.getText());
        Transport transport = session.getTransport();
        transport.connect(GMAIL_HOST, GMAIL_PORT, username.getText(), password.getText()); //gmail
        //Send email:
        transport.send(message);
        System.out.println("Message was sent successfully!");
        clearTextFields();
        transport.close();
    }

    /**
     * Creates a MimeMessage to send.
     * @param session
     * @param from
     * @param recipient
     * @return the MimeMessage created by input arguments.
     */
    private MimeMessage prepMessage(Session session, String from, String recipient) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject.getText());
            message.setText(messageText.getText());
            return message;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Sending message failed.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clear TextFields in GUI after sending email.
     */
    private void clearTextFields() {
        messageText.clear();
        username.clear();
        password.clear();
        from.clear();
        to.clear();;
        subject.clear();
    }


    public static void main(String[] args) throws Exception {
        launch(args);
    }
}