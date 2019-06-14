package snakeai;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Food extends Rectangle {
        Food (int _dim) {
            super((int)(Math.random()*40)*10, (int)(Math.random()*40)*10, _dim, _dim);
            setFill(Color.RED);
        }

        void randomize() {
            setX((int)(Math.random()*40)*10);
            setY((int)(Math.random()*40)*10);
        }

        double getDim() {
            return getWidth();
        }

}
