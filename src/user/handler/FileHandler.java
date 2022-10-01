package user.handler;

import user.User;

import java.io.*;
import java.util.LinkedList;

public class FileHandler {
    private final File file = new File(System.getProperty("user.dir"), "users.txt");

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
