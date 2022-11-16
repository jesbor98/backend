package whiteboard221;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uppgift221.Client;
import uppgift221.Point;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class WhiteboardJavaFX extends Application {

    private Canvas canvas;
    private Color color;

    @Override
    public void start(Stage stage) throws Exception {
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

    public HBox getHboxv1(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e-> {
            try {pointSketch(new Point2D(e.getX(), e.getY()), MouseEvent.MOUSE_PRESSED);
            } catch (Exception exception) {exception.printStackTrace();}
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e-> {
            try {pointSketch(new Point2D(e.getX(), e.getY()), MouseEvent.MOUSE_DRAGGED);
            } catch (Exception exception) {exception.printStackTrace();}
        });
        graphicsContext.setLineWidth(5);
        HBox hBox = new HBox();
        hBox.getChildren().add(canvas);
        hBox.setStyle("-fx-border-color:black;");
        return hBox;
    }

    public HBox getHboxv2(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        color = Color.BLACK;
        ColorPicker colorPicker = new ColorPicker(color);
        colorPicker.setOnAction(e-> {
            color = colorPicker.getValue();
            graphicsContext.setStroke(color);
        });

        Button button = new Button("Eraser");
        button.setOnAction(e-> {
            if(button.getText().equals("Eraser")) {
                color = Color.WHITE; //sudda
                graphicsContext.setStroke(color);
                colorPicker.setVisible(false);
                button.setText("Sketch");
                graphicsContext.setLineWidth(30);
                canvas.setCursor(Cursor.TEXT);
            } else {
                color = colorPicker.getValue();
                graphicsContext.setStroke(color);
                colorPicker.setVisible(true);
                button.setText("Eraser");
                graphicsContext.setLineWidth(5);
                canvas.setCursor(Cursor.DEFAULT);
            }
        });

        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.getChildren().addAll(button, colorPicker);
        return hbox;

    }

    public void pointSketch(Point2D point2D, EventType<MouseEvent> mouseEventEventType) {
        WhiteboardMessage message = null;
        if(this.color == Color.WHITE) message = new WhiteboardMessage(point2D, color, 50, mouseEventEventType);
        else message = new WhiteboardMessage(point2D, color, 5, mouseEventEventType);
        messageWhiteboard(message);
    }

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


    public void whiteboardPoint(Point2D point, EventType<MouseEvent> mouseEventEvent) {
        WhiteboardMessage whiteboardMessage = null; //send this
        //if(this.color)
    }


    public void getPoints() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
