package agh.ics.oop;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

    protected abstract char getIcon();

    @Override
    @Contract(" -> new")
    public final @NotNull String toString() {
        return Character.toString(getIcon());
    }

    public Vector2d getPosition() {
        return position;
    }
}
