package uppgift221;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.SocketException;

public class Whiteboard extends Application implements Runnable {

    private static final int LOCAL_PORT = 2000;
    private static final int REMOTE_PORT = 2001;
    private static final String REMOTE_HOST = "localhost";

    private Canvas canvas;
    private Color color;
    private Connection UDP = new Connection(LOCAL_PORT, REMOTE_HOST, REMOTE_PORT, this);

    public static void main(String[] args) throws SocketException {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(500, 500);
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: white;");
        vBox.getChildren().add(getHboxv1(canvas));
        vBox.getChildren().add(getHboxv2(canvas));
        stage.setTitle("Whiteboard Application");
        stage.setScene(new Scene(vBox, 490, 530));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method draws on the Whiteboard according to user input and sends the drawing as a DatagramPacket
     * by the UDP Connection.
     * @param canvas displaying on screen.
     * @return
     */
    public HBox getHboxv1(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e-> {
            try {
                draw(new Point2D(e.getX(), e.getY()), MouseEvent.MOUSE_PRESSED);
                UDP.sendIt((getMessage(new Point2D(e.getX(), e.getY()), MouseEvent.MOUSE_DRAGGED)));
            } catch (Exception exception) {exception.printStackTrace();}
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e-> {
            try {
                draw(new Point2D(e.getX(), e.getY()), MouseEvent.MOUSE_DRAGGED);
                UDP.sendIt(getMessage(new Point2D(e.getX(), e.getY()), MouseEvent.MOUSE_DRAGGED));
            } catch (Exception exception) {exception.printStackTrace();}
        });
        graphicsContext.setLineWidth(5);
        HBox hBox = new HBox();
        hBox.getChildren().add(canvas);
        hBox.setStyle("-fx-border-color:black;");
        return hBox;
    }

    /**
     * This method creates GUI and functional Buttons ("Draw"/"Erase").
     * @param canvas
     * @return
     */
    public HBox getHboxv2(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        color = Color.BLACK;
        ColorPicker colorPicker = new ColorPicker(color);
        colorPicker.setOnAction(e-> {
            color = colorPicker.getValue();
            graphicsContext.setStroke(color);
        });

        Button button = new Button("Erase");
        button.setOnAction(e-> {
            if(button.getText().equals("Erase")) {
                color = Color.WHITE; //sudda
                graphicsContext.setStroke(color);
                colorPicker.setVisible(false);
                button.setText("Draw");
                graphicsContext.setLineWidth(30);
                canvas.setCursor(Cursor.TEXT);
            } else {
                color = colorPicker.getValue();
                graphicsContext.setStroke(color);
                colorPicker.setVisible(true);
                button.setText("Erase");
                graphicsContext.setLineWidth(5);
                canvas.setCursor(Cursor.DEFAULT);
            }
        });

        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.getChildren().addAll(button, colorPicker);
        return hbox;

    }

    /**
     * This method creates a WhiteboardMessage depending on users chooise: "Draw"/"Erase".
     * @param point2D
     * @param mouseEvent
     */
    public void draw(Point2D point2D, EventType<MouseEvent> mouseEvent) {
        WhiteboardMessage message = null;
        if(this.color == Color.WHITE) {
            message = new WhiteboardMessage(point2D, color, 50, mouseEvent);
        } else {
            message = new WhiteboardMessage(point2D, color, 5, mouseEvent);
        }
        messageWhiteboard(message);
    }

    public WhiteboardMessage getMessage(Point2D point2D, EventType<MouseEvent> mouseEvent) {
        WhiteboardMessage message = null;
        if(this.color == Color.WHITE) {
            message = new WhiteboardMessage(point2D, color, 50, mouseEvent);
        } else {
            message = new WhiteboardMessage(point2D, color, 5, mouseEvent);
        }
        return message;
    }

    /**
     * This method uses a thread to listen for incoming drawings.
     */
    @Override
    public void run() {
        while (true) {
            UDP.receiveIt();
            //this.draw(p.getPoint2D(), p.getMouseEvent());
        }
    }

    /**
     * This method draws on whiteboard.
     * @param message that contains sketching information.
     */
    public void messageWhiteboard(WhiteboardMessage message) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if(message.getMouseEvent().equals(MouseEvent.MOUSE_PRESSED)) {
            graphicsContext.beginPath();
            graphicsContext.moveTo(message.getPoint2D().getX(), message.getPoint2D().getY());
        } else if(message.getMouseEvent().equals(MouseEvent.MOUSE_DRAGGED)) {
            graphicsContext.setLineWidth(message.getSize());
            graphicsContext.setStroke(message.getColor());
            graphicsContext.lineTo(message.getPoint2D().getX(), message.getPoint2D().getY());
            graphicsContext.stroke();
        }
    }
}
