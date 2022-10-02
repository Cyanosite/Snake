package game.panel;

import coordinate.Coordinate;
import game.frame.GameFrame;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

enum Direction {
    up, down, left, right
}

// TODO: Add scoreboard and edit alert to show personal best
public class GamePanel extends JPanel implements ActionListener {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 500;
    private static final int UNIT_SIZE = 25;
    private static final int HORIZONTAL_UNITS = PANEL_WIDTH / UNIT_SIZE;
    private static final int VERTICAL_UNITS = PANEL_HEIGHT / UNIT_SIZE;
    private static final int INITIAL_DELAY = 300;
    private final GameFrame frame;
    private final Timer timer = new Timer(INITIAL_DELAY, this);
    private final User user;
    private final Random random = new Random();
    private final ArrayList<Coordinate> snakeParts = new ArrayList<>();
    private Color snakeColor;
    private int score = 0;
    private Coordinate snakeHead;
    private Coordinate applePosition;
    private Direction snakeDirection;

    public GamePanel(GameFrame frame, User user) {
        this.frame = frame;
        this.user = user;
        setSnakeColor(user.color);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.addKeyListener(new GamePanel.ChangeDirectionKeyAdapter());
    }

    /**
     * Resets the position of the snake to the default top left
     * position facing right.
     */
    private void resetSnake() {
        snakeParts.clear();
        snakeParts.add(new Coordinate(0, 0));
        snakeParts.add(new Coordinate(1, 0));
        snakeParts.add(new Coordinate(2, 0));
        snakeHead = snakeParts.get(2);
        snakeDirection = Direction.right;
    }

    private void setSnakeColor(String color) {
        switch (color) {
            case "red" -> snakeColor = Color.RED;
            case "green" -> snakeColor = Color.GREEN;
            case "blue" -> snakeColor = Color.BLUE;
        }
    }

    /**
     * @return true if the apple's position is on the snake
     */
    private boolean isApplePositionInValid() {
        for (Coordinate part : snakeParts) {
            if (part.x == applePosition.x && part.y == applePosition.y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a new apple and puts it on the grid, the position of
     * this apple will always be on the grid and never on the snake.
     */
    private void spawnApple() {
        do {
            int xCoordinate = random.nextInt(HORIZONTAL_UNITS);
            int yCoordinate = random.nextInt(VERTICAL_UNITS);
            applePosition = new Coordinate(xCoordinate, yCoordinate);
        } while (isApplePositionInValid());
    }

    private void paintGrid(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        for (int i = 0; i <= PANEL_WIDTH; i += UNIT_SIZE) { // vertical lines
            graphics.drawLine(i, 0, i, PANEL_HEIGHT);
        }
        for (int i = 0; i <= PANEL_HEIGHT; i += UNIT_SIZE) { // horizontal lines
            graphics.drawLine(0, i, PANEL_WIDTH, i);
        }
    }

    private void paintApple(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillOval(applePosition.x * UNIT_SIZE, applePosition.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    private void paintSnake(Graphics graphics) {
        graphics.setColor(snakeColor);
        for (var part : snakeParts) {
            graphics.fillRect(part.x * UNIT_SIZE, part.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
        graphics.setColor(snakeColor.darker());
        graphics.fillRect(snakeHead.x * UNIT_SIZE, snakeHead.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.setBackground(Color.BLACK);
        paintGrid(graphics);
        paintApple(graphics);
        paintSnake(graphics);
    }

    /**
     * Called when the snake collides with the wall or itself.
     * Displays an alert where the user may choose whether they
     * want to continue or not.
     */
    private void gameOver() {
        timer.stop();
        String message;
        if (score > user.highScore) {
            user.highScore = score;
            frame.updateHighScore(score);
            message = "Congratulations! Your new High Score: " + score + " Start New Game?";
        } else {
            message = "Game Over! Your score: " + score + " Start New Game?";
        }
        Object[] options = {"Yes", "No"};
        int choice = JOptionPane.showOptionDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        switch (choice) {
            case 0 -> newGame();
            case 1 -> frame.dispose();
        }
    }

    /**
     * Extends the snake body by making the field of the apple into
     * a part of it. Lowers the timer delay making the snake move faster.
     */
    private void collectApple() {
        snakeParts.add(new Coordinate(applePosition));
        snakeHead = snakeParts.get(snakeParts.size() - 1);
        ++score;
        timer.setDelay((int) (timer.getDelay() * 0.95));
    }

    /**
     * @return true if the snake would go outside the grid
     */
    private boolean detectBorderCollision() {
        if (!headCanMove(snakeDirection)) {
            return true;
        }
        switch (snakeDirection) {
            case up -> {
                return snakeHead.y == 0;
            }
            case down -> {
                return snakeHead.y == VERTICAL_UNITS - 1;
            }
            case left -> {
                return snakeHead.x == 0;
            }
            case right -> {
                return snakeHead.x == HORIZONTAL_UNITS - 1;
            }
        }
        return false;
    }

    /**
     * @return true if the snake's head will be on the apple
     * the next time it moves.
     */
    private boolean detectAppleCollision() {
        switch (snakeDirection) {
            case up -> {
                return snakeHead.x == applePosition.x && snakeHead.y - 1 == applePosition.y;
            }
            case down -> {
                return snakeHead.x == applePosition.x && snakeHead.y + 1 == applePosition.y;
            }
            case left -> {
                return snakeHead.y == applePosition.y && snakeHead.x - 1 == applePosition.x;
            }
            case right -> {
                return snakeHead.y == applePosition.y && snakeHead.x + 1 == applePosition.x;
            }
        }
        return false;
    }

    /**
     * Moves the snake by 1 on the grid in the direction specified by the user
     */
    private void moveSnake() {
        Coordinate newHead = new Coordinate(snakeHead);
        switch (snakeDirection) {
            case up -> --newHead.y;
            case down -> ++newHead.y;
            case left -> --newHead.x;
            case right -> ++newHead.x;
        }
        snakeParts.add(newHead);
        snakeHead = newHead;
        snakeParts.remove(0);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (detectBorderCollision()) {
            gameOver();
        }
        if (detectAppleCollision()) {
            collectApple();
            spawnApple();
        } else {
            moveSnake();
        }
        repaint();
    }

    public void newGame() {
        resetSnake();
        spawnApple();
        timer.setDelay(INITIAL_DELAY);
        timer.start();
    }

    /**
     * @param direction the direction the user wants the snake to move toward
     * @return true if the snake isn't going towards itself
     */
    private boolean headCanMove(Direction direction) {
        switch (direction) {
            case up:
                for (Coordinate part : snakeParts) {
                    if (part.x == snakeHead.x && part.y == snakeHead.y - 1) {
                        return false;
                    }
                }
                break;
            case down:
                for (Coordinate part : snakeParts) {
                    if (part.x == snakeHead.x && part.y == snakeHead.y + 1) {
                        return false;
                    }
                }
                break;
            case left:
                for (Coordinate part : snakeParts) {
                    if (part.y == snakeHead.y && part.x == snakeHead.x - 1) {
                        return false;
                    }
                }
                break;
            case right:
                for (Coordinate part : snakeParts) {
                    if (part.y == snakeHead.y && part.x == snakeHead.x + 1) {
                        return false;
                    }
                }
                break;
        }
        return true;
    }

    /**
     * The snake can be controlled by the WASD keys.
     * This key listener sets the snake's direction depending on the
     * key pressed
     */
    class ChangeDirectionKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyChar()) {
                case 'w':
                    if (headCanMove(Direction.up)) {
                        snakeDirection = Direction.up;
                    }
                    break;
                case 's':
                    if (headCanMove(Direction.down)) {
                        snakeDirection = Direction.down;
                    }
                    break;
                case 'a':
                    if (headCanMove(Direction.left)) {
                        snakeDirection = Direction.left;
                    }
                    break;
                case 'd':
                    if (headCanMove(Direction.right)) {
                        snakeDirection = Direction.right;
                    }
                    break;
                case ' ':
                    newGame();
                    break;
            }
        }
    }
}
