package game.snake;

import coordinate.Coordinate;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

public class Snake {
    final Dimension map;
    public Direction direction;
    LinkedList<Coordinate> parts = new LinkedList<>();
    Coordinate head;

    public Snake(int width, int height) {
        map = new Dimension(width, height);
        this.reset();
    }

    public Coordinate getHead() {
        return head;
    }

    public LinkedList<Coordinate> getParts() {
        return parts;
    }

    /**
     * @param direction the direction the user wants the snake to move toward
     * @return true if the snake isn't going towards itself
     */
    public boolean headCanMove(Direction direction) {
        Coordinate partAfterHead = parts.get(parts.size() - 2);
        return switch (direction) {
            case up -> !(partAfterHead.x == head.x && partAfterHead.y == head.y - 1);
            case down -> !(partAfterHead.x == head.x && partAfterHead.y == head.y + 1);
            case left -> !(partAfterHead.y == head.y && partAfterHead.x == head.x - 1);
            case right -> !(partAfterHead.y == head.y && partAfterHead.x == head.x + 1);
        };
    }

    /**
     * Changes the snake's direction based on user input (Keyboard);
     *
     * @param key the user input
     */
    public void changeDirection(char key) {
        key = Character.toLowerCase(key);
        switch (key) {
            case 'w':
                if (headCanMove(Direction.up)) {
                    direction = Direction.up;
                }
                break;
            case 's':
                if (headCanMove(Direction.down)) {
                    direction = Direction.down;
                }
                break;
            case 'a':
                if (headCanMove(Direction.left)) {
                    direction = Direction.left;
                }
                break;
            case 'd':
                if (headCanMove(Direction.right)) {
                    direction = Direction.right;
                }
                break;
        }
    }

    /**
     * @return true if the snake collided with itself or the wall
     */
    public boolean collision() {
        return collisionWithBorder() || collisionWithBody();
    }

    /**
     * @return true if the snake collided with itself
     */
    boolean collisionWithBody() {
        for (var part : parts) {
            if (part.equals(head) && part != head) return true;
        }
        return false;
    }

    /**
     * @return true if the snake collided with the wall
     */
    boolean collisionWithBorder() {
        return switch (direction) {
            case up -> head.y == 0;
            case down -> head.y == map.height - 1;
            case left -> head.x == 0;
            case right -> head.x == map.width - 1;
        };
    }

    /**
     * Resets the position of the snake to the default top left
     * position facing right.
     */
    public void reset() {
        parts.clear();
        parts.add(new Coordinate(0, 0));
        parts.add(new Coordinate(1, 0));
        parts.add(new Coordinate(2, 0));
        head = parts.get(2);
        direction = Direction.right;
    }

    public void expand(Coordinate applePosition) {
        parts.add(new Coordinate(applePosition));
        head = parts.get(parts.size() - 1);
    }

    /**
     * Moves the snake by 1 on the grid in the direction specified by the user
     */
    public void move() {
        Coordinate newHead = new Coordinate(head);
        switch (direction) {
            case up -> --newHead.y;
            case down -> ++newHead.y;
            case left -> --newHead.x;
            case right -> ++newHead.x;
        }
        parts.add(newHead);
        head = newHead;
        parts.removeFirst();
    }

    /**
     * Flips the snake and starts moving it in the opposite direction;
     */
    public void flip() {
        Collections.reverse(parts);
        head = parts.getLast();
        switch (direction) {
            case up -> direction = headCanMove(Direction.down) ? Direction.down : Direction.up;
            case down -> direction = headCanMove(Direction.up) ? Direction.up : Direction.down;
            case left -> direction = headCanMove(Direction.right) ? Direction.right : Direction.left;
            case right -> direction = headCanMove(Direction.left) ? Direction.left : Direction.right;
        }
    }
}
