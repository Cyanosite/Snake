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

    @Override
    public boolean equals(Object obj) {
        User input = (User) obj;
        boolean nameEquals = this.name.equals(input.name);
        boolean colorEquals = this.color.equals(input.color);
        boolean highScoreEquals = this.highScore == input.highScore;
        return nameEquals && colorEquals && highScoreEquals;
    }
}
