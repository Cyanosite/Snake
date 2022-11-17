package game.panel;

import coordinate.Coordinate;
import game.apple.Apple;
import game.frame.GameFrame;
import game.snake.Snake;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener {
    static final int PANEL_WIDTH = 400;
    static final int PANEL_HEIGHT = 300;
    static final int UNIT_SIZE = 25;
    static final int HORIZONTAL_UNITS = PANEL_WIDTH / UNIT_SIZE;
    static final int VERTICAL_UNITS = PANEL_HEIGHT / UNIT_SIZE;
    static final int INITIAL_DELAY = 200;
    final GameFrame frame;
    final Timer timer = new Timer(INITIAL_DELAY, this);
    final User user;
    final Snake snake = new Snake(HORIZONTAL_UNITS, VERTICAL_UNITS);
    final Apple apple = new Apple(HORIZONTAL_UNITS, VERTICAL_UNITS);
    int score = 0;

    public GamePanel(GameFrame frame, User user) {
        this.frame = frame;
        this.user = user;
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.addKeyListener(new GamePanel.ChangeDirectionKeyAdapter());
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
        Coordinate applePosition = apple.getPosition();
        graphics.fillOval(applePosition.x * UNIT_SIZE, applePosition.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    private void paintSnake(Graphics graphics) {
        graphics.setColor(user.color);
        for (var part : snake.getParts()) {
            graphics.fillRect(part.x * UNIT_SIZE, part.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
        graphics.setColor(user.color.darker());
        Coordinate snakeHead = snake.getHead();
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

    public void gameStop() {
        timer.stop();
    }

    /**
     * Called when the game.snake collides with the wall or itself.
     * Displays an alert where the user may choose whether they
     * want to continue or not.
     */
    void gameOver() {
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
     * Extends the game.snake body by making the field of the apple into
     * a part of it. Lowers the timer delay making the game.snake move faster.
     */
    private void collectApple() {
        snake.expand(apple.getPosition());
        ++score;
        timer.setDelay((int) (timer.getDelay() * 0.98));
    }


    /**
     * @return true if the game.snake's head will be on the apple
     * the next time it moves.
     */
    boolean detectAppleCollision() {
        Coordinate applePosition = apple.getPosition();
        Coordinate snakeHead = snake.getHead();
        return switch (snake.direction) {
            case up -> snakeHead.x == applePosition.x && snakeHead.y - 1 == applePosition.y;
            case down -> snakeHead.x == applePosition.x && snakeHead.y + 1 == applePosition.y;
            case left -> snakeHead.y == applePosition.y && snakeHead.x - 1 == applePosition.x;
            case right -> snakeHead.y == applePosition.y && snakeHead.x + 1 == applePosition.x;
        };
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (snake.collision()) {
            gameOver();
        }
        if (detectAppleCollision()) {
            collectApple();
            apple.spawn(snake.getParts());
            snake.flip();
        } else {
            snake.move();
        }
        repaint();
    }

    public void newGame() {
        score = 0;
        snake.reset();
        apple.spawn(snake.getParts());
        timer.setDelay(INITIAL_DELAY);
        timer.start();
    }

    /**
     * The game.snake can be controlled by the WASD keys.
     * This key listener sets the game.snake's direction depending on the
     * key pressed
     */
    class ChangeDirectionKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            snake.changeDirection(keyEvent.getKeyChar());
        }
    }
}
