package login.panel;

import main.Main;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class LoginPanel extends JPanel {
    private final JTextField userName = new JTextField("your username");
    private final JComboBox<String> comboBox = new JComboBox<>();

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File save = new File((System.getProperty("user.dir")), "users.txt");
            User user = new User(userName.getText(), (String) comboBox.getSelectedItem());
            if (!save.exists()) {
                try {
                    if (!save.createNewFile()) {
                        System.out.println("Couldn't create file");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            if (save.exists()) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(save, true);
                    ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(user);
                    fileOutputStream.close();
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public LoginPanel() {
        // TODO: Add a way for users to select from existing users
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(new JLabel("username: "), constraints);
        constraints.gridx = 1;
        this.add(userName, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(new JLabel("Snake color: "), constraints);
        String[] comboBoxSelection = {"red", "green", "blue"};
        for (var item : comboBoxSelection) {
            comboBox.addItem(item);
        }
        constraints.gridx = 1;
        this.add(comboBox, constraints);
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ButtonListener());
        startButton.addActionListener(new Main.StartGameListener());
        constraints.gridy = 2;
        this.add(startButton, constraints);
    }
}
