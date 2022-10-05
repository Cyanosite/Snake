package user.handler;

import org.junit.Assert;
import user.User;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class FileHandlerTest {
    private final FileHandler fileHandler = new FileHandler();
    private final LinkedList<User> initialUsers = new LinkedList<>();
    private final LinkedList<User> actualUsers = new LinkedList<>();

    @org.junit.Before
    public void setUp() {
        fileHandler.file = new File(System.getProperty("user.dir"), "text.txt");
        initialUsers.addLast(new User("alma", Color.RED, 14));
        initialUsers.addLast(new User("szilva", Color.BLUE, 17));
        initialUsers.addLast(new User("szőlő", Color.GREEN, 13));
    }

    @org.junit.After
    public void tearDown() {
        fileHandler.file.deleteOnExit();
    }

    @org.junit.Test
    public void saveUsers() {
        actualUsers.clear();
        fileHandler.saveUsers(initialUsers);
        fileHandler.loadUsers(actualUsers);
        for (int i = 0; i < initialUsers.size(); ++i) {
            Assert.assertEquals(initialUsers.get(i), actualUsers.get(i));
        }
    }

    @org.junit.Test
    public void loadUsers() {
        actualUsers.clear();
        fileHandler.saveUsers(initialUsers);
        fileHandler.loadUsers(actualUsers);
        for (int i = 0; i < initialUsers.size(); ++i) {
            Assert.assertEquals(initialUsers.get(i), actualUsers.get(i));
        }
    }
}