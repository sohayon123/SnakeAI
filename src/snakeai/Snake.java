package snakeai;

import java.util.ArrayList;

public class Snake extends ArrayList<Snakepart> {

    Snake () {
        super();
        add(new Snakepart(Direction.UP, 200, 200, 10, true));
    }

    void add(Direction _direction, int _X, int _Y, int _multiplier) {
        add(new Snakepart(_direction, _X, _Y, _multiplier, false));
    }

    void move() {
        for (int i = size()-1; i >= 0; i--) {
            get(i).move();
            if (i > 0)
                get(i).setDirection(get(i-1).getDirection());
        }
    }

    void changeDirection(Direction _direction) {
        get(0).setDirection(_direction);
    }

    void stop() {
        for (Snakepart s : this) {
            s.setDirection(Direction.NONE);
        }
    }

}