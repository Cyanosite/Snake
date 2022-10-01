package main;


import game.frame.GameFrame;
import login.frame.LoginFrame;
import user.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final LoginFrame loginFrame = new LoginFrame();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User currentUser = loginFrame.getCurrentUser();
            if (currentUser != null) {
                GameFrame gameFrame = new GameFrame(currentUser, loginFrame.getUsers());
                loginFrame.setVisible(false);
                loginFrame.dispose();
                gameFrame.setVisible(true);
                gameFrame.newGame();
            }
        }
    }
}
