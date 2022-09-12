package GamePanel;

import Coordinate.Coordinate;

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

public class GamePanel extends JPanel implements ActionListener {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 500;
    private static final int UNIT_SIZE = 25;
    private static final int HORIZONTAL_UNITS = PANEL_WIDTH / UNIT_SIZE;
    private static final int VERTICAL_UNITS = PANEL_HEIGHT / UNIT_SIZE;
    private static final int INITIAL_DELAY = 300;
    private final JFrame frame;
    private final Timer timer;
    private int score = 0;
    private final Random random;
    private final ArrayList<Coordinate> snakeParts = new ArrayList<>();
    private Coordinate snakeHead;
    private Coordinate applePosition;
    private Direction snakeDirection;
    private void resetSnake() {
        snakeParts.clear();
        snakeParts.add(new Coordinate(0, 0));
        snakeParts.add(new Coordinate(1, 0));
        snakeParts.add(new Coordinate(2, 0));
        snakeHead = snakeParts.get(2);
        snakeDirection = Direction.right;
    }
    public GamePanel(JFrame frame) {
        this.frame = frame;
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.addKeyListener(new GamePanel.ChangeDirectionKeyAdapter());
        random = new Random();
        timer = new Timer(INITIAL_DELAY, this);
        timer.start();
        resetSnake();
        spawnApple();
    }

    private boolean isApplePositionInValid() {
        for (Coordinate part: snakeParts) {
            if (part.x == applePosition.x && part.y == applePosition.y) {
                return true;
            }
        }
        return false;
    }

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
        graphics.setColor(Color.GREEN);
        for (var part: snakeParts) {
            graphics.fillRect(part.x*UNIT_SIZE, part.y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            //graphics.fillRect(snakeParts.get(i).x*UNIT_SIZE, snakeParts.get(i).y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
        graphics.setColor(Color.decode("0x0CB312"));
        graphics.fillRect(snakeHead.x*UNIT_SIZE, snakeHead.y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.setBackground(Color.BLACK);
        paintGrid(graphics);
        paintApple(graphics);
        paintSnake(graphics);
    }

    private void gameOver() {
        timer.stop();
        Object[] options = {"Yes", "No"};
        int choice = JOptionPane.showOptionDialog(this, "Game Over! Your score: " + score + " Start New Game?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        switch (choice) {
            case 0 -> newGame();
            case 1 -> frame.dispose();
        }
    }

    private void collectApple() {
        snakeParts.add(new Coordinate(applePosition));
        snakeHead = snakeParts.get(snakeParts.size() - 1);
        ++score;
        timer.setDelay((int) (timer.getDelay()*0.95));
    }

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

    private void newGame() {
        resetSnake();
        spawnApple();
        timer.start();
    }

    private boolean headCanMove(Direction direction) {
        switch (direction) {
            case up:
                for (Coordinate part: snakeParts) {
                    if (part.x == snakeHead.x && part.y == snakeHead.y - 1) {
                        return false;
                    }
                }
                break;
            case down:
                for (Coordinate part: snakeParts) {
                    if (part.x == snakeHead.x && part.y == snakeHead.y + 1) {
                        return false;
                    }
                }
                break;
            case left:
                for (Coordinate part: snakeParts) {
                    if (part.y == snakeHead.y && part.x == snakeHead.x - 1) {
                        return false;
                    }
                }
                break;
            case right:
                for (Coordinate part: snakeParts) {
                    if (part.y == snakeHead.y && part.x == snakeHead.x + 1) {
                        return false;
                    }
                }
                break;
        }
        return true;
    }

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
