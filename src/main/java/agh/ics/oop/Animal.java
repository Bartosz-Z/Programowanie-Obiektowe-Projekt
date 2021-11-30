package agh.ics.oop;

import java.util.LinkedList;
import java.util.List;

public class Animal extends AbstractWorldMapElement {
    public final AbstractWorldMap map;
    private MapDirection mapDirection = MapDirection.NORTH;
    private final List<IPositionChangeObserver> observers = new LinkedList<>();

    public Animal(AbstractWorldMap map) {
        super(new Vector2d(2, 2));
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");

        this.map = map;
        addObserver(map);
    }

    public Animal(AbstractWorldMap map, Vector2d initialPosition) {
        super(initialPosition);
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");

        this.map = map;
        addObserver(map);
    }

    @Override
    protected char getIcon() {
        return mapDirection.toString().charAt(0);
    }

    private void tryChangingPosition(Vector2d newPosition) {
        if (map.isAccessible(newPosition)) {
            for (IPositionChangeObserver observer : observers)
                observer.positionChanged(this, position, newPosition);
            position = newPosition;
        }
    }

    public void move(MoveDirection direction) {
        if (direction == null)
            throw new IllegalArgumentException("Argument can not be null.");

        switch (direction) {
            case RIGHT -> mapDirection = mapDirection.next();
            case LEFT -> mapDirection = mapDirection.previous();
            case FORWARD -> tryChangingPosition(position.add(mapDirection.toUnitVector()));
            case BACKWARD -> tryChangingPosition(position.subtract(mapDirection.toUnitVector()));
        }
    }

    private void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    private void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public MapDirection getOrientation() {
        return mapDirection;
    }
}
