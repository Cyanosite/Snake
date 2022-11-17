package login;

import coordinate.Coordinate;
import game.snake.Snake;

import javax.swing.*;
import java.awt.*;

class SnakeColorPreview extends JPanel {
    private static final int UNIT_SIZE = 25;
    private final Snake snake;
    private Color color;

    public SnakeColorPreview(Color color) {
        this.color = color;
        this.snake = new Snake(3, 1);
        this.setPreferredSize(new Dimension(75, 25));
    }

    public void setColor(Color color) {
        this.color = color;
        this.repaint();
    }

    private void paintGrid(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        for (int i = 0; i <= 75; i += UNIT_SIZE) { // vertical lines
            graphics.drawLine(i, 0, i, UNIT_SIZE);
        }
        for (int i = 0; i <= UNIT_SIZE; i += UNIT_SIZE) { // horizontal lines
            graphics.drawLine(0, i, 75, i);
        }
    }

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
        paintGrid(graphics);
        paintSnake(graphics);
    }
}


