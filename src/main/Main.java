package main;


import game.frame.GameFrame;
import login.LoginFrame;
import user.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final LoginFrame loginFrame = new LoginFrame();
    private static GameFrame gameFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fires when the user clicks the Start button in the LoginFrame.
     * This disposes the LoginFrame and creates the GameFrame and starts
     * a new game.
     */
    public static class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User currentUser = loginFrame.getCurrentUser();
            if (currentUser != null) {
                gameFrame = new GameFrame(currentUser, loginFrame.getUsers());
                loginFrame.setVisible(false);
                loginFrame.dispose();
                gameFrame.setVisible(true);
                gameFrame.newGame();
            }
        }
    }

    /**
     * Fires when the user clicks the User -> Select User menu option.
     * This takes the user back to the LoginFrame.
     */
    public static class SelectUser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginFrame.pack();
            gameFrame.gameStop();
            gameFrame.setVisible(false);
            gameFrame.dispose();
            loginFrame.setVisible(true);
        }
    }
}
