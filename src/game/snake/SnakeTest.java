package game.snake;

import coordinate.Coordinate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

public class SnakeTest {
    private final Snake snake = new Snake(16, 12);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Snake going upwards and tries to go back into itself
     */
    @Test
    public void headCanMoveUp() {
        snake.parts = new LinkedList<>();
        snake.parts.add(new Coordinate(0, 2));
        snake.parts.add(new Coordinate(0, 1));
        snake.parts.add(new Coordinate(0, 0));
        snake.head = snake.parts.get(2);
        snake.direction = Direction.up;
        Assert.assertFalse(snake.headCanMove(Direction.down));
    }

    /**
     * Snake going downwards and tries to go back into itself
     */
    @Test
    public void headCanMoveDown() {
        snake.parts = new LinkedList<>();
        snake.parts.add(new Coordinate(0, 0));
        snake.parts.add(new Coordinate(0, 1));
        snake.parts.add(new Coordinate(0, 2));
        snake.head = snake.parts.get(2);
        snake.direction = Direction.down;
        Assert.assertFalse(snake.headCanMove(Direction.up));
    }

    /**
     * Snake going towards the left and tries to go back into itself
     */
    @Test
    public void headCanMoveLeft() {
        snake.parts = new LinkedList<>();
        snake.parts.add(new Coordinate(2, 0));
        snake.parts.add(new Coordinate(1, 0));
        snake.parts.add(new Coordinate(0, 0));
        snake.head = snake.parts.get(2);
        snake.direction = Direction.left;
        Assert.assertFalse(snake.headCanMove(Direction.right));
    }

    /**
     * Snake going towards the right and tries to go back into itself
     */
    @Test
    public void headCanMoveRight() {
        snake.parts = new LinkedList<>();
        snake.parts.add(new Coordinate(0, 0));
        snake.parts.add(new Coordinate(1, 0));
        snake.parts.add(new Coordinate(2, 0));
        snake.head = snake.parts.get(2);
        snake.direction = Direction.right;
        Assert.assertFalse(snake.headCanMove(Direction.left));
    }

    @Test
    public void changeDirectionToUp() {
        snake.changeDirection('W');
        Assert.assertSame(Direction.up, snake.direction);
        snake.changeDirection('w');
        Assert.assertSame(Direction.up, snake.direction);
    }

    @Test
    public void changeDirectionToDown() {
        snake.changeDirection('S');
        Assert.assertSame(Direction.down, snake.direction);
        snake.changeDirection('s');
        Assert.assertSame(Direction.down, snake.direction);
    }

    @Test
    public void changeDirectionToLeft() {
        // Would fail normally because snake would move back into itself
        snake.parts.clear();
        snake.parts.add(new Coordinate(2, 0));
        snake.parts.add(new Coordinate(1, 0));
        snake.parts.add(new Coordinate(0, 0));
        snake.head = snake.parts.get(2);
        snake.changeDirection('A');
        Assert.assertSame(Direction.left, snake.direction);
        snake.changeDirection('a');
        Assert.assertSame(Direction.left, snake.direction);
        snake.reset();
    }

    @Test
    public void changeDirectionToRight() {
        snake.changeDirection('D');
        Assert.assertSame(Direction.right, snake.direction);
        snake.changeDirection('d');
        Assert.assertSame(Direction.right, snake.direction);
    }

    @Test
    public void collisionWithBody() {
        snake.parts.clear();
        snake.parts.add(new Coordinate(0, 0));
        snake.parts.add(new Coordinate(1, 0));
        snake.parts.add(new Coordinate(2, 0));
        snake.parts.add(new Coordinate(2, 1));
        snake.parts.add(new Coordinate(1, 1));
        snake.parts.add(new Coordinate(1, 0));
        snake.head = snake.parts.getLast();
        Assert.assertTrue(snake.collisionWithBody());
    }

    @Test
    public void collisionWithBorder() {
        snake.parts.clear();
        snake.parts.add(new Coordinate(2, 0));
        snake.parts.add(new Coordinate(1, 0));
        snake.parts.add(new Coordinate(0, 0));
        snake.direction = Direction.left;
        snake.head = snake.parts.getLast();
        Assert.assertTrue(snake.collisionWithBorder());
    }

    @Test
    public void expand() {
        snake.reset();
        Coordinate newHead = new Coordinate(3, 0);
        snake.expand(newHead);
        Assert.assertEquals(snake.head, newHead);
        Assert.assertEquals(snake.parts.getFirst(), new Coordinate(0, 0));
    }

    @Test
    public void move() {
        snake.reset();
        Coordinate newHead = new Coordinate(3, 0);
        snake.move();
        Assert.assertEquals(newHead, snake.head);
        Assert.assertEquals(new Coordinate(1, 0), snake.parts.getFirst());
    }

    @Test
    public void flip() {
        snake.reset();
        snake.flip();
        Assert.assertEquals(new Coordinate(0, 0), snake.head);
        Assert.assertEquals(Direction.left, snake.direction);
    }
}