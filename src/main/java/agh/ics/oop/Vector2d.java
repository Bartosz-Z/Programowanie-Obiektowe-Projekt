package agh.ics.oop;

public record Vector2d(int x, int y) {

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return x >= other.x && y >= other.y;
    }

    public Vector2d lowerLeft(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2d upperRight(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d add(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }
}
