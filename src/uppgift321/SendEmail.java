package uppgift321;
//Code w JavaFX:
//https://github.com/ugurkebir/JavaFX-Mail-Sender/blob/master/Main.java
/**
 * Using SMTP (Simple Mail Transfer Protocol) to send Email.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Properties;

public class SendEmail extends Application {

    private Session session;
    private TextField username;
    private TextField password;
    private TextField from;
    private TextField to;
    private TextField subject;
    private TextField emailServer;
    private TextArea messageText;

    /**
     * Launch application and GUI.
     * When user presses the Send-Button - the email will send.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Send Email");
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        root.setOrientation(Orientation.VERTICAL);

        Label label = new Label("Send an email");
        label.setAlignment(Pos.CENTER);

        //EMAIL SPECIFIC INFO:
        emailServer = new TextField();
        emailServer.setPromptText("Server name: ");
        username = new TextField();
        username.setPromptText("Username: ");
        password = new TextField();
        password.setPromptText("Password: ");

        //TEXTFIELDS:
        from = new TextField();
        from.setPromptText("From: ");
        to = new TextField();
        to.setPromptText("To: ");
        subject = new TextField();
        subject.setPromptText("Subject: ");

        messageText = new TextArea();
        messageText.setPromptText("Message: ");

        getProperties();

        Button sendButton = new Button("Send"); //set on action and send it
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
        root.getChildren().addAll(label, emailServer, username, password, from, to, subject, messageText, sendButton);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Apply properties for sending email.
     */
    public void getProperties() {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", true); //gmail
        properties.put("mail.smtp.host", emailServer.getText()); //connect to gmail (prev: emailServer.getText())
        properties.put("mail.smtp.port", 465); //port 465 eller (587 = gmail)
        properties.put("mail.smtp.starttls.enable", true); //et tls encryption enabled
        properties.put("mail.transport.protocol", "smtp");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username.getText(), password.getText());
            }
        });
    }

    /**
     * Send email with the MimeMessage class by prepping the message using the session, sender and receiver.
     * @throws MessagingException
     */
    public void sendEmail() throws MessagingException {
        MimeMessage message = prepMessage(session, from.getText(), to.getText());
        //Send email:
        Transport.send(message);
        System.out.println("Message was sent successfully!");
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
        emailServer.clear();
        subject.clear();
    }


    public static void main(String[] args) throws MessagingException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    launch(args);
                    SendEmail sender = new SendEmail();
                    sender.clearTextFields();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}