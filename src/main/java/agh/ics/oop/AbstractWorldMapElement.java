package agh.ics.oop;

import agh.ics.oop.observers.IOnDestroyInvokeObserver;
import agh.ics.oop.observers.IOnDestroyObservable;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldMapElement implements IOnDestroyObservable {
    protected Vector2d position;
    protected final List<IOnDestroyInvokeObserver> onDestroyObservers;

    protected AbstractWorldMapElement(Vector2d initialPosition) {
        Ensure.Not.Null(initialPosition, "element's initial position");

        position = initialPosition;
        onDestroyObservers = new LinkedList<>();
    }

    public boolean isAt(Vector2d position) {
        Ensure.Not.Null(position, "position");

        return this.position.equals(position);
    }

    public abstract int getLayer();

    public abstract ImageName getImageName();

    public void destroy() {
        for (IOnDestroyInvokeObserver observer : onDestroyObservers)
            observer.onElementDestroy(this);
    }

    @Override
    public void addObserver(IOnDestroyInvokeObserver observer) {
        onDestroyObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnDestroyInvokeObserver observer) {
        onDestroyObservers.remove(observer);
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
