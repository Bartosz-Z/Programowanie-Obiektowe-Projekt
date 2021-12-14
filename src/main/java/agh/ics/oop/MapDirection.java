package agh.ics.oop;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public MapDirection next() {
        switch (this) {
            case NORTH -> { return NORTH_EAST; }
            case NORTH_EAST -> { return EAST; }
            case EAST -> { return SOUTH_EAST; }
            case SOUTH_EAST -> { return SOUTH; }
            case SOUTH -> { return SOUTH_WEST; }
            case SOUTH_WEST -> { return WEST; }
            case WEST -> { return NORTH_WEST; }
            case NORTH_WEST -> { return NORTH; }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH -> { return NORTH_WEST; }
            case NORTH_WEST -> { return WEST; }
            case WEST -> { return SOUTH_WEST; }
            case SOUTH_WEST -> { return SOUTH; }
            case SOUTH -> { return SOUTH_EAST; }
            case SOUTH_EAST -> { return EAST; }
            case EAST -> { return NORTH_EAST; }
            case NORTH_EAST -> { return NORTH; }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }

    @Contract(" -> new")
    public @NotNull Vector2d toUnitVector() {
        switch (this) {
            case NORTH -> { return new Vector2d(0, 1); }
            case NORTH_EAST -> { return new Vector2d(1, 1); }
            case EAST -> { return new Vector2d(1, 0); }
            case SOUTH_EAST -> { return new Vector2d(1, -1); }
            case SOUTH -> { return new Vector2d(0, -1); }
            case SOUTH_WEST -> { return new Vector2d(-1, -1); }
            case WEST -> { return new Vector2d(-1, 0); }
            case NORTH_WEST -> { return new Vector2d(-1, 1); }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        switch (this) {
            case NORTH -> { return "North"; }
            case NORTH_EAST -> { return "North-East"; }
            case EAST -> { return "East"; }
            case SOUTH_EAST -> { return "South-East"; }
            case SOUTH -> { return "South"; }
            case SOUTH_WEST -> { return "South-West"; }
            case WEST -> { return "West"; }
            case NORTH_WEST -> { return "North-West"; }
        }
        throw new UnsupportedOperationException("'" + this + "' is not implemented.");
    }
}
