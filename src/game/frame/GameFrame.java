package game.frame;

import game.leaderboard.Leaderboard;
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

        gamePanel = new GamePanel(this, user);
        this.add(gamePanel, BorderLayout.WEST);

        // Leaderboard setup
        Object[][] userData = new Object[users.size()][2];
        userIndex = users.indexOf(user);
        for (int i = 0; i < users.size(); ++i) {
            User temp = users.get(i);
            userData[i][0] = temp.name;
            userData[i][1] = temp.highScore;
        }
        leaderboard = new Leaderboard(userData);
        this.add(leaderboard.scrollPane, BorderLayout.EAST);

        // Frame setup
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 540));
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Updates the leaderboard table with the new high score and updates
     * the storage of the users with it.
     *
     * @param newHighScore the newly achieved high score of the user
     */
    public void updateHighScore(int newHighScore) {
        leaderboard.table.setValueAt(newHighScore, userIndex, 1);
        users.get(userIndex).highScore = newHighScore;
        fileHandler.saveUsers(users);
    }

    public void newGame() {
        gamePanel.newGame();
    }
}
