package agh.ics.oop;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Vector2d(int x, int y) {

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        return "(" + x + "," + y + ")";
    }

    @Contract("null -> fail; !null -> _")
    public boolean precedes(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return x <= other.x && y <= other.y;
    }

    @Contract("null -> fail; !null -> _")
    public boolean follows(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return x >= other.x && y >= other.y;
    }

    @Contract("null -> fail; !null -> new")
    public @NotNull Vector2d lowerLeft(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    @Contract("null -> fail; !null -> new")
    public @NotNull Vector2d upperRight(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    @Contract("null -> fail; !null -> new")
    public @NotNull Vector2d add(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(x + other.x, y + other.y);
    }

    @Contract("null -> fail; !null -> new")
    public @NotNull Vector2d subtract(Vector2d other) {
        if (other == null)
            throw new IllegalArgumentException("Argument can not be null.");
        return new Vector2d(x - other.x, y - other.y);
    }

    @Contract(" -> new")
    public @NotNull Vector2d opposite() {
        return new Vector2d(-x, -y);
    }
}
