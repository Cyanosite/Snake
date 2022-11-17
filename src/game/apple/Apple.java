package game.apple;

import coordinate.Coordinate;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Apple {
    final Dimension map;
    private final Random random = new Random();
    Coordinate position;

    public Apple(int width, int height) {
        map = new Dimension(width, height);
    }

    public Coordinate getPosition() {
        return position;
    }

    /**
     * Generates a new apple and puts it on the grid, the position of
     * this apple will always be on the grid and never on the game.snake.
     */
    public void spawn(LinkedList<Coordinate> parts) {
        do {
            int xCoordinate = random.nextInt(map.width);
            int yCoordinate = random.nextInt(map.height);
            position = new Coordinate(xCoordinate, yCoordinate);
        } while (isPositionInvalid(parts));
    }

    /**
     * @return true if the apple's position is on the snake
     */
    boolean isPositionInvalid(LinkedList<Coordinate> parts) {
        for (Coordinate part : parts) {
            if (part.equals(position)) {
                return true;
            }
        }
        return false;
    }
}
