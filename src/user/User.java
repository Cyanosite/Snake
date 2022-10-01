package user;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String color;
    public int highScore;

    public User(String name, String color, int highScore) {
        this.name = name;
        this.color = color;
        this.highScore = highScore;
    }
}
