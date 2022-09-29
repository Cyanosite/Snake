package game.frame;

import game.panel.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final GamePanel gamePanel = new GamePanel(this);

    public void newGame() {
        gamePanel.newGame();
    }

    public GameFrame() {
        super("Snake");
        this.add(gamePanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 540));
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
