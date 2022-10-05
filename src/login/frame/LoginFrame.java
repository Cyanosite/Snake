package login.frame;

import login.panel.LoginPanel;
import user.User;

import javax.swing.*;
import java.util.LinkedList;

public class LoginFrame extends JFrame {
    private final LoginPanel loginPanel = new LoginPanel();

    public LoginFrame() {
        super("Login");
        this.add(loginPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    public User getCurrentUser() {
        return loginPanel.getCurrentUser();
    }

    public LinkedList<User> getUsers() {
        return loginPanel.getUsers();
    }
}
