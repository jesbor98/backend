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

public class EmailGUI extends Application {

    private ArrayList<TextField> emailInfoInput = new ArrayList<>();
    private ArrayList<TextField> messageInfoInput = new ArrayList<>();

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
        TextField username = new TextField();
        username.setPromptText("Username: ");
        emailInfoInput.add(username);
        TextField password = new TextField();
        password.setPromptText("Password: ");
        emailInfoInput.add(password);

        //TEXTFIELDS:
        TextField from = new TextField();
        from.setPromptText("From: ");
        messageInfoInput.add(from);
        TextField to = new TextField();
        to.setPromptText("To: ");
        messageInfoInput.add(to);
        TextField subject = new TextField();
        subject.setPromptText("Subject: ");
        messageInfoInput.add(subject);

        TextArea messageText = new TextArea();
        messageText.setPromptText("Message: ");

        Button sendButton = new Button("Send Email"); //set on action and send it
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Properties properties = new Properties();
                properties.put("mail.smtp.auth", true);
                properties.put("mail.smtp.host", emailServer.getText());
                properties.put("mail.smtp.port", 587); //port 465 eller 587
                properties.put("mail.smpt.starttls.enable", true);
                properties.put("mail.transport.protocol", "smtp");

                //Authenticator = object that obtain information for a network connection
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    //getPassword(...) is called when password authorization is needed, needen
                    // when signing in to an account
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username.getText(), password.getText());
                    }
                });

                Message message = new MimeMessage(session);
                try {
                    Address toAdress = new InternetAddress(to.getText());
                    Address fromAdress = new InternetAddress(from.getText());
                    message.setFrom(fromAdress);
                    message.setRecipient(Message.RecipientType.TO, toAdress);
                    message.setSubject(subject.getText());
                    message.setText(messageText.getText());

                    Transport.send(message);
                    System.out.println("Email sent!");
                } catch (AddressException e) {
                    e.printStackTrace();
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

    public static void main(String[] args) {
        launch(args);
    }
}
