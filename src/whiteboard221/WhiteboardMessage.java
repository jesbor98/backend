package whiteboard221;

import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

//Contains sketching information from client -> server!
public class WhiteboardMessage {

    private Point2D point2D; //point to be sketch
    private Color color;
    private int size;
    private EventType<MouseEvent> mouseEvent; //mouse pressed & mouse dragged

    //constructor:
    //tar in och omvandlar till message:
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
