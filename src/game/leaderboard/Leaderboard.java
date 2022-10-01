package game.leaderboard;

import javax.swing.*;
import java.awt.*;

public class Leaderboard {
    public final JScrollPane scrollPane;
    public final JTable table;

    public Leaderboard(Object[][] data) {
        table = new JTable(data, new String[]{"User", "HighScore"});
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(200, 540));
    }
}
