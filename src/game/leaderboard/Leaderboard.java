package game.leaderboard;

import user.User;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Leaderboard extends JScrollPane {
    final JTable table;
    String[] columnNames = {"User", "HighScore"};

    public Leaderboard(LinkedList<User> users) {
        Object[][] userData = new Object[users.size()][2];
        for (int i = 0; i < users.size(); ++i) {
            userData[i] = users.get(i).toArray();
        }
        table = new JTable(userData, columnNames);
        this.setViewportView(table);
        this.setPreferredSize(new Dimension(200, 300));
    }

    public void updateScoreAt(int index, int score) {
        table.setValueAt(score, index, 1);
    }
}
