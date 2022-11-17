package login;

import main.Main;
import user.User;
import user.handler.FileHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class LoginFrame extends JFrame {
    static final FileHandler fileHandler = new FileHandler();
    final JTextField userName = new JTextField("your username");
    final DefaultListModel<String> listModel = new DefaultListModel<>();
    final JList<String> userList = new JList<>(listModel);
    final JColorChooser colorChooser = new JColorChooser();
    final SnakeColorPreview snakeColorPreview = new SnakeColorPreview(colorChooser.getColor());
    LinkedList<User> users = new LinkedList<>();
    User currentUser;

    public LoginFrame() {
        super("Login");
        //this.add(loginPanel);
        this.setLayout(new GridBagLayout());
        // Grid constraint setup
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        colorChooser.setPreviewPanel(snakeColorPreview);
        colorChooser.getSelectionModel().addChangeListener(new SnakeColorPreviewUpdate());
        this.add(colorChooser, constraints);

        // creating the user selector list
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(5);
        userList.getSelectionModel().addListSelectionListener(new listListener());
        users = fileHandler.loadUsers();
        updateUserList();
        constraints.gridx = 0;
        constraints.gridy = 1;
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        this.add(scrollPane, constraints);

        // Login section
        // Username input
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(new JLabel("username: "), constraints);
        constraints.gridx = 1;
        this.add(userName, constraints);

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

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
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

    public User getCurrentUser() {
        return currentUser;
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    private class SnakeColorPreviewUpdate implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (currentUser == null) return;
            Color color = colorChooser.getColor();
            snakeColorPreview.setColor(color);
            currentUser.color = color;
            fileHandler.saveUsers(users);
        }
    }

    /**
     * Modifies the contents of currentUser to the
     * current selected item in the user list.
     */
    public class listListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) return;
            if (users.size() == 0) return;
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            currentUser = users.get(lsm.getMinSelectionIndex());
            snakeColorPreview.setColor(currentUser.color);
        }
    }

    /**
     * Adds the user to the list of users then updates
     * the user storage.
     */
    private class AddUserButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentUser = new User(userName.getText(), colorChooser.getColor(), 0);
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
