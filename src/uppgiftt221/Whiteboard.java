package uppgiftt221;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uppgift221.Client;
import uppgift221.Point;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Whiteboard extends Application {

    private Pane pane;
    private Canvas canvas;
    private Scene scene;
    private double x, y;
    private GraphicsContext graphicsContext;

    @Override
    public void start(Stage stage) throws Exception {

        pane = new Pane();
        scene = new Scene(pane, 800, 500);
        //we draw on canvas using GraphicsContext
        canvas = new Canvas(800, 500);
        ColorPicker colorPicker = new ColorPicker();
        Slider slider = new Slider();
        Label label = new Label("1.0");
        GridPane gridPane = new GridPane();


        {
            graphicsContext = canvas.getGraphicsContext2D(); //instansiate the gc-object
            graphicsContext.setStroke(javafx.scene.paint.Color.BLACK); //same as colorpicker
            graphicsContext.setLineWidth(1);

            colorPicker.setValue(Color.BLACK); // same color in the beginning
            colorPicker.setOnAction(e -> {
                graphicsContext.setStroke(colorPicker.getValue());
            });

            slider.setMin(1);
            slider.setMax(100);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.valueProperty().addListener(e -> {
                double value = slider.getValue();
                String str = String.format("%.1f", value); //string with 1 decimal
                label.setText(str);
                graphicsContext.setLineWidth(value);
            });

            gridPane.addRow(0, colorPicker, slider, label);
            gridPane.setHgap(20);
            gridPane.setAlignment(Pos.TOP_CENTER);
            gridPane.setPadding(new Insets(20, 0, 0, 0));

            //pane.setOnMousePressed(new MouseStart());
            //pane.setOnMouseDragged(new MouseDragged());

            //Drawing functionality:
            //this is how you place a dot,

            /*scene.setOnMousePressed(e -> {
                //x1 = getSceneX, y1 = getSceneY
                graphicsContext.beginPath(); //reset path, previous coordinates will be the same as the new coordinates
                graphicsContext.lineTo(e.getSceneX(), e.getSceneY()); //register the new coordinates
                graphicsContext.stroke(); //draw the line according to path
            });*/
            pane.setOnMousePressed(new MouseStart());
            pane.setOnMouseDragged(new MouseDragged());

            /*scene.setOnMouseDragged(e -> {
                graphicsContext.lineTo(e.getSceneX(), e.getSceneY());
                graphicsContext.stroke();

            });*/
            pane.getChildren().addAll(canvas, gridPane);

        }
        stage.setScene(scene);
        stage.show();
    }


    class MouseStart implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            graphicsContext.beginPath(); //reset path, previous coordinates will be the same as the new coordinates
            graphicsContext.lineTo(mouseEvent.getSceneX(), mouseEvent.getSceneY()); //register the new coordinates
            graphicsContext.stroke(); //draw the line according to path
            //x = mouseEvent.getX();
            //y = mouseEvent.getY();
        }
    }

    class MouseDragged implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            graphicsContext.lineTo(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            graphicsContext.stroke();
            //double newX = pane.getLayoutX() + mouseEvent.getX() - x;
            //double newY = pane.getLayoutY() + mouseEvent.getY() - y;
            //pane.relocate(newX, newY);
        }
    }

    public void getPoints() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}

