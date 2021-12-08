package agh.ics.oop;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public MapDirection next() {
        switch (this) {
            case NORTH -> { return EAST; }
            case EAST -> { return SOUTH; }
            case SOUTH -> { return WEST; }
            case WEST -> { return NORTH; }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH -> { return WEST; }
            case EAST -> { return NORTH; }
            case SOUTH -> { return EAST; }
            case WEST -> { return SOUTH; }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }

    @Contract(" -> new")
    public @NotNull Vector2d toUnitVector() {
        switch (this) {
            case NORTH -> { return new Vector2d(0, 1); }
            case SOUTH -> { return new Vector2d(0, -1); }
            case WEST -> { return new Vector2d(-1, 0); }
            case EAST -> { return new Vector2d(1, 0); }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }


    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        switch (this) {
            case NORTH -> { return "North"; }
            case SOUTH -> { return "South"; }
            case WEST -> { return "West"; }
            case EAST -> { return "East"; }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }
}
