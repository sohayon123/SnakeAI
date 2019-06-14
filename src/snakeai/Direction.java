package snakeai;

import javafx.geometry.Point2D;

public class Direction extends Point2D {

    public final static Direction LEFT = new Direction(-1, 0);
    public final static Direction RIGHT = new Direction(1, 0);
    public final static Direction UP = new Direction(0, -1);
    public final static Direction DOWN = new Direction(0, 1);
    public final static Direction NONE = new Direction(0, 0);

    Direction (int _X, int _Y) {
        super(_X, _Y);
    }
}