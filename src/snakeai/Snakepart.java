package snakeai;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Snakepart extends Rectangle {
    Direction direction;
    boolean isHead;

    Snakepart (Direction _direction, int _X, int _Y, int _multiplier, boolean _isHead) {
        super(_X, _Y, _multiplier, _multiplier);
        isHead = _isHead;
        if (isHead)
            setFill(Color.YELLOW);
        else
            setFill(Color.RED);
        direction = _direction;
    }

    void setDirection(Direction _direction) {
            direction = _direction;
    }

    Direction getDirection() {
        return direction;
    }

    boolean isHead() {
        return isHead;
    }

    double getDim() {
        return getWidth();
    }

    void move() {
        setX(getX()+direction.getX()*getDim());
        setY(getY()+direction.getY()*getDim());
    }
}