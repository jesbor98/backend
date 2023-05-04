package uppgift312;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class GuestbookGUI extends JFrame  {
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel websiteLabel;
    private JLabel commentLabel;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField websiteField;
    private JTextArea commentArea;
    private JButton submitButton;
    private Connection conn;

    private GuestbookDatabase db = new GuestbookDatabase();


    public GuestbookGUI() throws SQLException {
        // Skapa Swing-komponenter för GUI
        nameLabel = new JLabel("Namn:");
        emailLabel = new JLabel("E-post:");
        websiteLabel = new JLabel("Hemsida:");
        commentLabel = new JLabel("Kommentar:");
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        websiteField = new JTextField(20);
        commentArea = new JTextArea(5, 20);
        submitButton = new JButton("Skicka in");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String website = websiteField.getText();
                String comment = commentArea.getText();

                GuestbookEntry entry = new GuestbookEntry(name, email, website, comment);
                try {
                    db.addEntry(entry);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // Lägg till Swing-komponenter till JFrame-komponenten
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(websiteLabel);
        panel.add(websiteField);
        panel.add(commentLabel);
        panel.add(commentArea);
        panel.add(submitButton);

        getContentPane().add(panel, BorderLayout.CENTER);

        // Anslut till databasen
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/guestbook", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Visa JFrame-komponenten
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws SQLException {
        GuestbookGUI guestbookGUI = new GuestbookGUI();
    }
}
