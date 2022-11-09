package game.panel;

import coordinate.Coordinate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;

import java.awt.*;
import java.util.LinkedList;

public class GamePanelTest {
    private final LinkedList<Coordinate> snakeFacingItself = new LinkedList<>();
    GamePanel gamePanel = new GamePanel(null, new User("test", Color.GREEN, 1));

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void detectBorderCollisionLeft() {
        gamePanel.snakeHead = new Coordinate(0, 1);
        gamePanel.snakeDirection = Direction.left;
        Assert.assertTrue(gamePanel.detectBorderCollision());
    }

    @Test
    public void detectBorderCollisionRight() {
        gamePanel.snakeHead = new Coordinate(GamePanel.HORIZONTAL_UNITS - 1, 2);
        gamePanel.snakeDirection = Direction.right;
        Assert.assertTrue(gamePanel.detectBorderCollision());
    }

    @Test
    public void detectBorderCollisionTop() {
        gamePanel.snakeHead = new Coordinate(3, 0);
        gamePanel.snakeDirection = Direction.up;
        Assert.assertTrue(gamePanel.detectBorderCollision());
    }

    @Test
    public void detectBorderCollisionBottom() {
        gamePanel.snakeHead = new Coordinate(3, GamePanel.VERTICAL_UNITS - 1);
        gamePanel.snakeDirection = Direction.down;
        Assert.assertTrue(gamePanel.detectBorderCollision());
    }


    @Test
    public void detectAppleCollision() {
        gamePanel.snakeHead = new Coordinate(3, 2);
        gamePanel.snakeDirection = Direction.left;
        gamePanel.applePosition = new Coordinate(2, 2);
        Assert.assertTrue(gamePanel.detectAppleCollision());
    }

    @Test
    public void headCanMove() {
        snakeFacingItself.add(new Coordinate(2, 2));
        snakeFacingItself.add(new Coordinate(3, 2));
        gamePanel.snakeParts = snakeFacingItself;
        gamePanel.snakeHead = snakeFacingItself.get(0);
        Assert.assertFalse(gamePanel.headCanMove(Direction.right));
    }
}