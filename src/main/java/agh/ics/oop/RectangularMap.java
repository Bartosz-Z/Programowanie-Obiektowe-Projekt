package agh.ics.oop;

public class RectangularMap extends AbstractWorldMap {
    private final int height, width;

    public RectangularMap(int width, int height) {
        super();

        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isAccessible(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        if (!(position.follows(new Vector2d(0, 0)) && position.precedes(new Vector2d(width, height))))
            return false;
        return !isOccupied(position);
    }
}
