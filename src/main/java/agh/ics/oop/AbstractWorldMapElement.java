package agh.ics.oop;

public abstract class AbstractWorldMapElement {
    protected Vector2d position;

    protected AbstractWorldMapElement(Vector2d initialPosition) {
        if (initialPosition == null)
            throw new IllegalArgumentException("'initialPosition' argument can not be null.");

        position = initialPosition;
    }

    public boolean isAt(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        return this.position.equals(position);
    }

    public abstract int getLayer();

    public abstract ImageName getImageName();

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
