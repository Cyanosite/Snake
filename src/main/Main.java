package main;


import game.frame.GameFrame;
import login.frame.LoginFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final LoginFrame loginFrame = new LoginFrame();
    private static final GameFrame gameFrame = new GameFrame();

    public static class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginFrame.setVisible(false);
            gameFrame.setVisible(true);
            gameFrame.newGame();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Couldn't set look and feel");
        }
    }
}
