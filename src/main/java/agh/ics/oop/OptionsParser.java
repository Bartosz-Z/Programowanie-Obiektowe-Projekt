package agh.ics.oop;

import java.util.Arrays;

public class OptionsParser {
    public MoveDirection[] parse(String[] strings) {
        if (strings == null)
            return new MoveDirection[0];

        return Arrays.stream(strings).map(string -> {
            switch (string) {
                case "f", "forward" -> { return MoveDirection.FORWARD; }
                case "b", "backward" -> { return MoveDirection.BACKWARD; }
                case "r", "right" -> { return MoveDirection.RIGHT; }
                case "l", "left" -> { return MoveDirection.LEFT; }
            }
            throw new IllegalArgumentException(string + " is not legal move specification");
        }).toArray(MoveDirection[]::new);
    }
}
