package login.panel;

import main.Main;
import user.User;
import user.handler.FileHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class LoginPanel extends JPanel {
    private final FileHandler fileHandler = new FileHandler();
    private final JTextField userName = new JTextField("your username");
    private final JComboBox<String> comboBox = new JComboBox<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> userList = new JList<>(listModel);
    private final LinkedList<User> users = new LinkedList<>();
    private User currentUser;

    public LoginPanel() {
        this.setLayout(new GridBagLayout());
        // Grid constraint setup
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // creating the user selector list
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(5);
        userList.addListSelectionListener(new listListener());
        fileHandler.loadUsers(users);
        updateUserList();
        constraints.gridwidth = 2;
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        this.add(scrollPane, constraints);

        // Login section
        // Username input
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(new JLabel("username: "), constraints);
        constraints.gridx = 1;
        this.add(userName, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;

        // Snake color selector
        this.add(new JLabel("Snake color: "), constraints);
        String[] comboBoxSelection = {"red", "green", "blue"};
        for (var item : comboBoxSelection) {
            comboBox.addItem(item);
        }
        constraints.gridx = 1;
        this.add(comboBox, constraints);

        // Add user, Remove user buttons
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(new AddUserButtonListener());
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(addUserButton, constraints);
        JButton removeUserButton = new JButton("Remove User");
        removeUserButton.addActionListener(new RemoveUserButtonListener());
        constraints.gridx = 1;
        this.add(removeUserButton, constraints);

        // Start game button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new Main.StartGameListener());
        constraints.gridy = 4;
        constraints.gridx = 1;
        this.add(startButton, constraints);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    /**
     * Sets the elements of the user list to
     * the contents of users.
     */
    private void updateUserList() {
        listModel.removeAllElements();
        for (var user : users) {
            listModel.addElement(user.name);
        }
    }

    /**
     * Modifies the contents of currentUser to the
     * current selected item in the user list.
     */
    public class listListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                if (users.size() == 0) currentUser = null;
                else currentUser = users.get(e.getFirstIndex());
            }
        }
    }

    /**
     * Adds the user to the list of users then updates
     * the user storage.
     */
    private class AddUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentUser = new User(userName.getText(), (String) comboBox.getSelectedItem(), 0);
            users.addLast(currentUser);
            listModel.addElement(currentUser.name);
            fileHandler.saveUsers(users);
        }
    }

    /**
     * Removes the user from the list of users then updates
     * the user storage.
     */
    private class RemoveUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            users.remove(userList.getSelectedIndex());
            currentUser = null;
            listModel.remove(userList.getSelectedIndex());
            fileHandler.saveUsers(users);
        }
    }
}
