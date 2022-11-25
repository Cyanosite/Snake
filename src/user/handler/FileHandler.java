package user.handler;

import user.User;

import java.io.*;
import java.util.LinkedList;

public class FileHandler {
    static File file = new File(System.getProperty("user.dir"), "users.txt");

    /**
     * Save the users to a txt file in a Serialized way
     *
     * @param users the collection of the users to save
     */
    public void saveUsers(LinkedList<User> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(users);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads the users from the users.txt file
     *
     * @return always returns a LinkedList of Users,
     * if something went wrong it will be empty
     */
    public LinkedList<User> loadUsers() {
        LinkedList<User> users = new LinkedList<>();
        if (!file.exists()) return users;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            users = (LinkedList<User>) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
