package user;

import java.awt.*;
import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public Color color;
    public int highScore;

    public User(String name, Color color, int highScore) {
        this.name = name;
        this.color = color;
        this.highScore = highScore;
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;
        User input = (User) obj;
        boolean nameEquals = this.name.equals(input.name);
        boolean colorEquals = this.color.equals(input.color);
        boolean highScoreEquals = this.highScore == input.highScore;
        return nameEquals && colorEquals && highScoreEquals;
    }

    public Object[] toArray() {
        return new Object[]{name, highScore};
    }
}
