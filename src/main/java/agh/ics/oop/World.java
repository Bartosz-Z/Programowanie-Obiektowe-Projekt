package agh.ics.oop;

import java.util.Arrays;

public class World {
    public static void main(String[] args) {
        System.out.println("Start");
        Direction[] directions = processDirections(args);
        run(directions);
        System.out.println("Stop");
    }

    private static Direction[] processDirections(String[] items) {
        return Arrays.stream(items).map(item -> {
            switch (item) {
                case "f" -> { return Direction.FORWARD; }
                case "b" -> { return Direction.BACKWARD; }
                case "r" -> { return Direction.RIGHT; }
                case "l" -> { return Direction.LEFT; }
            }
            return null;
        }).toArray(Direction[]::new);
    }

    private static void run(Direction[] directions) {
        Arrays.stream(directions).forEach(direction -> {
            switch (direction) {
                case FORWARD -> System.out.println("Zwierzak idzie do przodu.");
                case BACKWARD -> System.out.println("Zwierzak idzie do tyłu.");
                case RIGHT -> System.out.println("Zwierzak idzie w prawo.");
                case LEFT -> System.out.println("Zwierzak idzie w lewo.");
            }
        });
    }
}
