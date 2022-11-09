package game.frame;

import game.leaderboard.Leaderboard;
import game.menu.Menu;
import game.panel.GamePanel;
import user.User;
import user.handler.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class GameFrame extends JFrame {
    private final FileHandler fileHandler = new FileHandler();
    private final Leaderboard leaderboard;
    private final LinkedList<User> users;
    private final int userIndex;
    private final GamePanel gamePanel;

    public GameFrame(User user, LinkedList<User> users) {
        super("Snake - " + user.name);
        this.users = users;

        // Menu setup
        this.add(new Menu(), BorderLayout.NORTH);

        // Game setup
        gamePanel = new GamePanel(this, user);
        this.add(gamePanel, BorderLayout.WEST);

        // Leaderboard setup
        leaderboard = new Leaderboard(users);
        userIndex = users.indexOf(user);
        this.add(leaderboard, BorderLayout.EAST);

        // Frame setup
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void gameStop() {
        gamePanel.gameStop();
    }

    /**
     * Updates the leaderboard table with the new high score and updates
     * the storage of the users with it.
     *
     * @param score the newly achieved high score of the user
     */
    public void updateHighScore(int score) {
        leaderboard.updateScoreAt(userIndex, score);
        users.get(userIndex).highScore = score;
        fileHandler.saveUsers(users);
    }

    public void newGame() {
        gamePanel.newGame();
    }
}
