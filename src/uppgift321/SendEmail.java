package uppgift321;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class SendEmail extends Application {

    private ArrayList<TextField> emailInfoInput = new ArrayList<>();
    private ArrayList<TextField> messageInfoInput = new ArrayList<>();

    private TextField username;
    private TextField password;
    private TextField from;
    private TextField to;
    private TextField subject;
    private TextArea messageText;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Send Email");
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER);
        root.setOrientation(Orientation.VERTICAL);

        Label label = new Label("Send an email");
        label.setAlignment(Pos.CENTER);

        //EMAIL SPECIFIC INFO:
        TextField emailServer = new TextField();
        emailServer.setPromptText("Server name: ");
        emailInfoInput.add(emailServer);
        username = new TextField();
        username.setPromptText("Username: ");
        emailInfoInput.add(username);
        password = new TextField();
        password.setPromptText("Password: ");
        emailInfoInput.add(password);

        //TEXTFIELDS:
        from = new TextField();
        from.setPromptText("From: ");
        messageInfoInput.add(from);
        to = new TextField();
        to.setPromptText("To: ");
        messageInfoInput.add(to);
        subject = new TextField();
        subject.setPromptText("Subject: ");
        messageInfoInput.add(subject);

        messageText = new TextArea();
        messageText.setPromptText("Message: ");

        Button sendButton = new Button("Send"); //set on action and send it
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    sendEmail(to.getText());
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

    public void sendEmail(String recipient) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", true); //gmail
        properties.put("mail.smtp.host", "smtp.gmail.com"); //connect to gmail (prev: emailServer.getText())
        properties.put("mail.smtp.port", 465); //port 465 eller (587 = gmail)
        properties.put("mail.smpt.starttls.enable", true); //et tls encryption enabled
        properties.put("mail.transport.protocol", "smtp");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username.getText(), password.getText());
            }
        });

        Message message = prepMessage(session, from.getText(), recipient);

        //Send email:
        Transport.send(message);
        System.out.println("Message was sent successfully!");
    }

    private Message prepMessage(Session session, String from, String recipient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject.getText());
            message.setText(messageText.getText());
            return message;

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {
        launch(args);
    }
}