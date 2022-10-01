package login.panel;

import main.Main;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.channels.FileLockInterruptionException;
import java.util.LinkedList;

public class LoginPanel extends JPanel {
    private final JTextField userName = new JTextField("your username");
    private final JComboBox<String> comboBox = new JComboBox<>();
    private final File save = new File((System.getProperty("user.dir")), "users.txt");
    private final LinkedList<User> users = new LinkedList<>();
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                    outputStream.close();
                    fileOutputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    private void fillUsers(DefaultListModel<String> userList) {
        if (save.exists()) {
            FileInputStream fileInputStream = null;
            ObjectInputStream inputStream = null;
            try {
                 fileInputStream = new FileInputStream(save);
                 inputStream = new ObjectInputStream(fileInputStream);
                User user = (User) inputStream.readObject();
                while (true) {
                    if (user != null) users.addLast(user);
                    user = (User) inputStream.readObject();
                }
            } catch (EOFException eofException) {
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (fileInputStream != null) fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (var user : users) {
                userList.addElement(user.name);
            }
        }
    }
    public LoginPanel() {
        // TODO: Add a way for users to select from existing users
        this.setLayout(new GridBagLayout());
        // Grid constraint setup
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // creating the user selector
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        fillUsers(defaultListModel);
        JList<String> userList = new JList<>(defaultListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(5);
        constraints.gridwidth = 2;
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        this.add(scrollPane, constraints);

        // Login section
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(new JLabel("username: "), constraints);
        constraints.gridx = 1;
        this.add(userName, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
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
        constraints.gridy = 3;
        this.add(startButton, constraints);
    }
}
