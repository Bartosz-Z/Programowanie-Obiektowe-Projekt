package agh.ics.oop;

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
        return null;
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH -> { return WEST; }
            case EAST -> { return NORTH; }
            case SOUTH -> { return EAST; }
            case WEST -> { return SOUTH; }
        }
        return null;
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH -> { return new Vector2d(0, 1); }
            case SOUTH -> { return new Vector2d(0, -1); }
            case WEST -> { return new Vector2d(-1, 0); }
            case EAST -> { return new Vector2d(1, 0); }
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case NORTH -> { return "North"; }
            case SOUTH -> { return "South"; }
            case WEST -> { return "West"; }
            case EAST -> { return "East"; }
        }
        return null;
    }
}
