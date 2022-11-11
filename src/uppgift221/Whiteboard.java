package uppgift221;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.awt.*;
import java.net.Socket;

import static javafx.application.Application.launch;


// YOUTUBE VIDEO FOR SENDING THE DRAWING VIA UDP:
// https://www.youtube.com/watch?v=MRg2JW6c-ew&ab_channel=VladimirPintea
public class Whiteboard extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    Pane pane = new Pane();
    Scene scene = new Scene(pane, 800, 500);

    Canvas canvas = new Canvas(800, 500); //we draw on canvas using GraphicsContext
    GraphicsContext graphicsContext;
    ColorPicker colorPicker = new ColorPicker();
    Slider slider = new Slider();
    Label label = new Label("1.0");
    GridPane gridPane = new GridPane();


    {
        graphicsContext = canvas.getGraphicsContext2D(); //instansiate the gc-object
        graphicsContext.setStroke(Color.BLACK); //same as colorpicker
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

        //Drawing functionality:
        //this is how you place a dot,
        scene.setOnMousePressed(e -> {
            //x1 = getSceneX, y1 = getSceneY
            graphicsContext.beginPath(); //reset path, previous coordinates will be the same as the new coordinates
            graphicsContext.lineTo(e.getSceneX(), e.getSceneY()); //register the new coordinates
            graphicsContext.stroke(); //draw the line according to path
        });

        scene.setOnMouseDragged(e -> {
            graphicsContext.lineTo(e.getSceneX(), e.getSceneY());
            graphicsContext.stroke();

        });
        pane.getChildren().addAll(canvas, gridPane);

    }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
