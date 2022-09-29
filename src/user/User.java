package user;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String color;

    public User(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
