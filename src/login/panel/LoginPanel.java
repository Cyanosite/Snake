package login.panel;

import main.Main;
import user.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;

public class LoginPanel extends JPanel {
    private final JTextField userName = new JTextField("your username");
    private final JComboBox<String> comboBox = new JComboBox<>();
    private final File save = new File((System.getProperty("user.dir")), "users.txt");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final LinkedList<User> users = new LinkedList<>();
    private JList<String> userList = new JList<>(listModel);
    private User currentUser;

    public LoginPanel() {
        this.setLayout(new GridBagLayout());
        // Grid constraint setup
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        // creating the user selector
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(5);
        userList.addListSelectionListener(new listListener());
        loadUsers();
        updateUserList();
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
        JButton newUserButton = new JButton("Add User");
        newUserButton.addActionListener(new ButtonListener());
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(newUserButton, constraints);
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new Main.StartGameListener());
        constraints.gridx = 1;
        this.add(startButton, constraints);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    private void updateUserList() {
        for (int i = listModel.getSize(); i < users.size(); ++i) {
            listModel.addElement(users.get(i).name);
        }
    }

    private void saveUsers() {
        if (save.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(save);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                for (var user : users) {
                    outputStream.writeObject(user);
                }
                outputStream.close();
                fileOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                if (save.createNewFile()) {
                    saveUsers();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadUsers() {
        if (save.exists()) {
            users.clear();
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
            } catch (EOFException ignored) {
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
        }
    }

    public class listListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting())
                currentUser = users.get(e.getFirstIndex());
        }
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentUser = new User(userName.getText(), (String) comboBox.getSelectedItem());
            users.addLast(currentUser);
            saveUsers();
            updateUserList();
        }
    }
}
