package agh.ics.oop;

public abstract class AbstractWorldMapElement {
    protected Vector2d position;

    protected AbstractWorldMapElement(Vector2d initialPosition) {
        Ensure.Not.Null(initialPosition, "element's initial position");

        position = initialPosition;
    }

    public boolean isAt(Vector2d position) {
        Ensure.Not.Null(position, "position");

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
