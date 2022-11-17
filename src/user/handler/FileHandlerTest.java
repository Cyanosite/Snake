package user.handler;

import org.junit.Assert;
import user.User;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class FileHandlerTest {
    private final LinkedList<User> initialUsers = new LinkedList<>();
    private FileHandler fileHandler;

    @org.junit.Before
    public void setUp() {
        fileHandler = new FileHandler();
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
        fileHandler.saveUsers(initialUsers);
    }

    @org.junit.Test
    public void loadUsers() {
        LinkedList<User> actualUsers = fileHandler.loadUsers();
        Assert.assertNotNull(actualUsers);
        for (int i = 0; i < initialUsers.size(); ++i) {
            Assert.assertEquals(initialUsers.get(i), actualUsers.get(i));
        }
    }
}