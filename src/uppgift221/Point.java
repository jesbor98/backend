package uppgift221;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class Point extends BorderPane {

    private double x, y;

    public Point(float x, float y) {
        setOnMousePressed(new DrawStart());
        setOnMouseDragged(new DrawHandler());
    }

    class DrawHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double newX = getLayoutX() + mouseEvent.getX() - x;
            double newY = getLayoutY() + mouseEvent.getY() - y;
            relocate(newX, newY);
        }
    }

    class DrawStart implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            x = mouseEvent.getX();
            y = mouseEvent.getY();
        }
    }
}
