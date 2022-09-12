package Main;


import GameFrame.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println("Couldn't set look and feel");
        }
        GameFrame gameFrame = new GameFrame();
    }
}