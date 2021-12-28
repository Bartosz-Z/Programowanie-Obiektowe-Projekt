package agh.ics.oop.elements;

import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.observers.*;
import agh.ics.oop.utility.Ensure;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWorldMapDynamicElement
        extends AbstractWorldMapElement
        implements IMovable, IPositionObservable, IDirectionObservable {
    protected final AbstractWorldMap map;
    protected MapDirection mapDirection;
    protected final List<IPositionChangeObserver> positionObservers;
    protected final List<IDirectionChangeObserver> directionObservers;

    protected AbstractWorldMapDynamicElement(AbstractWorldMap map, Vector2d initialPosition) {
        super(initialPosition);

        Ensure.Not.Null(map, "world map");
        this.map = map;

        this.mapDirection = MapDirection.values()[ThreadLocalRandom.current().nextInt(MapDirection.values().length)];
        positionObservers = new LinkedList<>();
        directionObservers = new LinkedList<>();
    }

    public boolean tryChangePosition(Vector2d newPosition) {
        if (map.isAccessible(newPosition)) {
            for (IPositionChangeObserver observer : positionObservers)
                observer.positionChanged(this, position, newPosition);
            position = newPosition;
            return true;
        }
        return false;
    }

    protected void changeDirection(MapDirection newMapDirection) {
        mapDirection = newMapDirection;
        for (IDirectionChangeObserver observer : directionObservers)
            observer.directionChanged(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        positionObservers.clear();
        directionObservers.clear();
    }

    @Override
    public void addObserver(IPositionChangeObserver observer) {
        positionObservers.add(observer);
    }

    @Override
    public void removeObserver(IPositionChangeObserver observer) {
        positionObservers.remove(observer);
    }

    @Override
    public void addObserver(IDirectionChangeObserver observer) {
        directionObservers.add(observer);
    }

    @Override
    public void removeObserver(IDirectionChangeObserver observer) {
        directionObservers.remove(observer);
    }

    @Override
    public MapDirection getOrientation() {
        return mapDirection;
    }
}
