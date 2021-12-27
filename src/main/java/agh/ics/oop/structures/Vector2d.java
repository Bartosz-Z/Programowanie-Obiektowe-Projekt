package agh.ics.oop.structures;

import agh.ics.oop.utility.Ensure;

public record Vector2d(int x, int y) {

    public final static Vector2d zero = new Vector2d(0, 0);

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other) {
        Ensure.Not.Null(other, "other vector2d");

        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2d other) {
        Ensure.Not.Null(other, "other vector2d");

        return x >= other.x && y >= other.y;
    }

    public Vector2d lowerLeft(Vector2d other) {
        Ensure.Not.Null(other, "other vector2d");

        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2d upperRight(Vector2d other) {
        Ensure.Not.Null(other, "other vector2d");

        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d add(Vector2d other) {
        Ensure.Not.Null(other, "other vector2d");

        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        Ensure.Not.Null(other, "other vector2d");

        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }
}
