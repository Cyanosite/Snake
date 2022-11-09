package user.handler;

import coordinate.Coordinate;
import game.panel.Direction;
import user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class FileHandler {
    File file = new File(System.getProperty("user.dir"), "users.txt");

    public void saveUsers(LinkedList<User> users) {
        if (file.exists()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
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
                if (file.createNewFile()) {
                    saveUsers(users);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveUserProgression(User user, ArrayList<Coordinate> snakeParts, Coordinate snakeHead, Coordinate applePosition, Direction snakeDirection, double timerDelay) {
        File userProgressionFile = new File(System.getProperty("user.dir"), user.name);
        try {
            if (file.exists() || file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(userProgressionFile);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                outputStream.writeObject(user);
                outputStream.writeObject(snakeParts);
                outputStream.writeObject(snakeHead);
                outputStream.writeObject(applePosition);
                outputStream.writeObject(snakeDirection);
                outputStream.writeObject(timerDelay);
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public GameState loadUserProgression() {

    }*/

    public void loadUsers(LinkedList<User> users) {
        if (file.exists()) {
            users.clear();
            FileInputStream fileInputStream = null;
            ObjectInputStream inputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                inputStream = new ObjectInputStream(fileInputStream);
                User user = (User) inputStream.readObject();
                while (true) {
                    if (user != null) users.addLast(user);
                    else break;
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
}
