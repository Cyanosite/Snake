package game.menu;

import main.Main;

import javax.swing.*;

public class Menu extends JMenuBar {
    public Menu() {
        JMenu menu = new JMenu("User");
        JMenuItem menuItem = new JMenuItem("Select User");
        menuItem.addActionListener(new Main.SelectUser());
        menu.add(menuItem);
        this.add(menu);
    }
}
