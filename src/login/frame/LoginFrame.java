package login.frame;

import login.panel.LoginPanel;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        super("Login");
        this.add(new LoginPanel());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(500, 500));
        this.setResizable(false);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }
}
