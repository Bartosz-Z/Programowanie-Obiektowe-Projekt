package agh.ics.oop;

import java.util.Arrays;
import java.util.Objects;

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
            return null;
        }).filter(Objects::nonNull).toArray(MoveDirection[]::new);
    }
}
