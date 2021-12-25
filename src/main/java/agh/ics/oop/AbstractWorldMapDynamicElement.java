package agh.ics.oop;

import agh.ics.oop.observers.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractWorldMapDynamicElement
        extends AbstractWorldMapElement
        implements IMovable, IPositionObservable, IDirectionObservable, IOnDestroyObservable {
    protected final AbstractWorldMap map;
    protected MapDirection mapDirection;
    protected final List<IPositionChangeObserver> positionObservers;
    protected final List<IDirectionChangeObserver> directionObservers;
    protected final List<IOnDestroyInvokeObserver> onDestroyObservers;

    protected AbstractWorldMapDynamicElement(AbstractWorldMap map, Vector2d initialPosition) {
        super(initialPosition);

        Ensure.Not.Null(map, "world map");
        this.map = map;

        this.mapDirection = MapDirection.values()[ThreadLocalRandom.current().nextInt(MapDirection.values().length)];
        positionObservers = new LinkedList<>();
        directionObservers = new LinkedList<>();
        onDestroyObservers = new LinkedList<>();
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
    public void addObserver(IOnDestroyInvokeObserver observer) {
        onDestroyObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnDestroyInvokeObserver observer) {
        onDestroyObservers.remove(observer);
    }

    public void destroy() {
        for (IOnDestroyInvokeObserver observer : onDestroyObservers)
            observer.onElementDestroy(this);
    }

    @Override
    public MapDirection getOrientation() {
        return mapDirection;
    }
}
