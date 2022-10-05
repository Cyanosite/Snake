package login.panel;

import coordinate.Coordinate;
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
import java.util.ArrayList;
import java.util.LinkedList;

public class LoginPanel extends JPanel {
    private final FileHandler fileHandler = new FileHandler();
    private final JTextField userName = new JTextField("your username");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> userList = new JList<>(listModel);
    private final LinkedList<User> users = new LinkedList<>();
    private final JColorChooser colorChooser = new JColorChooser();
    private final SnakeColorPreview snakeColorPreview = new SnakeColorPreview(colorChooser.getColor());
    private User currentUser;

    public LoginPanel() {

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
        userList.addListSelectionListener(new listListener());
        fileHandler.loadUsers(users);
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

    private static class SnakeColorPreview extends JPanel {
        private final ArrayList<Coordinate> snakeParts = new ArrayList<>();
        private final Coordinate snakeHead;
        private final int UNIT_SIZE = 25;
        private Color color;

        public SnakeColorPreview(Color color) {
            this.color = color;
            snakeParts.add(new Coordinate(0, 0));
            snakeParts.add(new Coordinate(1, 0));
            snakeParts.add(new Coordinate(2, 0));
            snakeHead = snakeParts.get(2);
            this.setPreferredSize(new Dimension(75, 25));
            this.repaint();
        }

        public void setColor(Color color) {
            this.color = color;
        }

        private void paintGrid(Graphics graphics) {
            graphics.setColor(Color.GRAY);
            for (int i = 0; i <= 75; i += UNIT_SIZE) { // vertical lines
                graphics.drawLine(i, 0, i, UNIT_SIZE);
            }
            for (int i = 0; i <= UNIT_SIZE; i += UNIT_SIZE) { // horizontal lines
                graphics.drawLine(0, i, 75, i);
            }
        }

        private void paintSnake(Graphics graphics) {
            graphics.setColor(color);
            for (var part : snakeParts) {
                graphics.fillRect(part.x * UNIT_SIZE, part.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
            graphics.setColor(color.darker());
            graphics.fillRect(snakeHead.x * UNIT_SIZE, snakeHead.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            this.setBackground(Color.BLACK);
            paintGrid(graphics);
            paintSnake(graphics);
        }
    }

    private class SnakeColorPreviewUpdate implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            snakeColorPreview.setColor(colorChooser.getColor());
            snakeColorPreview.repaint();
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
