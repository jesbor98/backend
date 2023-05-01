package uppgift214;

import java.net.Socket;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.JLabel;
import java.io.ByteArrayOutputStream;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel imageLabel;
    private BufferedImage selectedImage;

    public Client() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.NORTH);

        JButton btnSelectImage = new JButton("Select Image");
        buttonPanel.add(btnSelectImage);

        imageLabel = new JLabel("");
        contentPane.add(imageLabel, BorderLayout.CENTER);

        JButton btnSendImage = new JButton("Send Image");
        buttonPanel.add(btnSendImage);

        btnSelectImage.addActionListener(e -> selectImage());
        btnSendImage.addActionListener(e -> sendImage());
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                selectedImage = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(selectedImage.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImage() {
        if (selectedImage != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(selectedImage, "jpg", baos);
                baos.flush();
                byte[] bytes = baos.toByteArray();
                baos.close();

                Socket socket = new Socket("localhost", Server.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(bytes);
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client frame = new Client();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}


