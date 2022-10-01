package game.frame;

import game.panel.GamePanel;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class GameFrame extends JFrame {
    private final GamePanel gamePanel;

    public GameFrame(User user, LinkedList<User> users) {
        super("Snake - " + user.name);
        gamePanel = new GamePanel(this, user, users);
        this.add(gamePanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 540));
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void newGame() {
        gamePanel.newGame();
    }
}
