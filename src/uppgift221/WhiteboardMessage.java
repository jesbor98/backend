package uppgift221;

import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.Serializable;

//Contains sketching information from client -> server!
public class WhiteboardMessage implements Serializable {

    private Point2D point2D; //point to be sketch
    private Color color;
    private int size;
    private EventType<MouseEvent> mouseEvent; //mouse pressed & mouse dragged

    /**
     * This method creates a WhiteboardMessage to be displayed on screen depending on user input.
     * @param point2D
     * @param color
     * @param size
     * @param mouseEvent
     */
    public WhiteboardMessage(Point2D point2D, Color color, int size, EventType<MouseEvent> mouseEvent) {
        this.point2D = point2D;
        this.color = color;
        this.size = size;
        this.mouseEvent = mouseEvent;
    }

    public Point2D getPoint2D(){return point2D;}

    public Color getColor() {return color;}

    public int getSize(){return size;}

    public EventType<MouseEvent> getMouseEvent() {return mouseEvent;}

}
