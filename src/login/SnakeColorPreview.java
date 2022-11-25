package login;

import coordinate.Coordinate;
import game.snake.Snake;

import javax.swing.*;
import java.awt.*;

/**
 * This overrides the default preview of the ColorPicker class
 * to show a Snake instead.
 */
class SnakeColorPreview extends JPanel {
    private static final int UNIT_SIZE = 25;
    private final Snake snake;
    private Color color;

    public SnakeColorPreview(Color color) {
        this.color = color;
        // set the Size of the preview to the default Snake size
        this.snake = new Snake(3, 1);
        this.setPreferredSize(new Dimension(75, 25));
    }

    public void setColor(Color color) {
        this.color = color;
        this.repaint();
    }

    /**
     * Draw the snake on the panel
     *
     * @param graphics given by paintComponent
     */
    private void paintSnake(Graphics graphics) {
        graphics.setColor(color);
        for (var part : snake.getParts()) {
            graphics.fillRect(part.x * UNIT_SIZE, part.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
        graphics.setColor(color.darker());
        Coordinate snakeHead = snake.getHead();
        graphics.fillRect(snakeHead.x * UNIT_SIZE, snakeHead.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.setBackground(Color.BLACK);
        paintSnake(graphics);
    }
}


