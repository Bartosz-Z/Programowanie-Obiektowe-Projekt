package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Class<? extends AbstractWorldMapElement>, Map<Vector2d, AbstractWorldMapElement>> worldMapElements;
    private final MapVisualizer mapVisualizer;
    private final MapBoundary mapBoundary;

    protected AbstractWorldMap() {
        mapBoundary = new MapBoundary();
        worldMapElements = new HashMap<>();
        mapVisualizer = new MapVisualizer(this);
    }

    @Override
    public abstract boolean isAccessible(Vector2d position);

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        for (Map<Vector2d, AbstractWorldMapElement> elements : worldMapElements.values()) {
            AbstractWorldMapElement element = elements.get(position);
            if (element != null)
                return element;
        }
        return null;
    }

    public <T extends AbstractWorldMapElement>
    AbstractWorldMapElement objectOfClassAt(Class<T> elementClass, Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        Map<Vector2d, ? extends AbstractWorldMapElement> elements = worldMapElements.get(elementClass);
        if (elements != null)
            for (AbstractWorldMapElement element : elements.values())
                if (element.isAt(position))
                    return element;

        return null;
    }

    public void place(AbstractWorldMapElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        Map<Vector2d, AbstractWorldMapElement> elements =
                worldMapElements.computeIfAbsent(element.getClass(), k -> new HashMap<>());

        if (!isAccessible(element.getPosition()))
            throw new IllegalArgumentException("Cannot place place element at not accessible field.");
        elements.put(element.getPosition(), element);

        if (element instanceof IObservable) {
            ((IObservable) element).addObserver(this);
            ((IObservable) element).addObserver(mapBoundary);
        }

        mapBoundary.addPosition(element.getPosition());
    }

    @Override
    @Deprecated
    public boolean place(Animal animal) {
        this.place((AbstractWorldMapElement) animal);
        return true;
    }

    public <T extends AbstractWorldMapElement>
    Map<Vector2d, ? extends AbstractWorldMapElement> getWorldMapElements(Class<T> elementClass) {
        return worldMapElements.get(elementClass);
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");
        if (oldPosition == null)
            throw new IllegalArgumentException("'oldPosition' argument can not be null.");
        if (newPosition == null)
            throw new IllegalArgumentException("'newPosition' argument can not be null.");

        Map<Vector2d, AbstractWorldMapElement> elements = worldMapElements.get(element.getClass());
        if (elements == null || elements.remove(oldPosition) == null)
            throw new IllegalArgumentException("Such an element is not placed in the map.");

        elements.put(newPosition, element);
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(mapBoundary.getLowerLeft(), mapBoundary.getUpperRight());
/*        Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (Map<Vector2d, AbstractWorldMapElement> elements : worldMapElements.values())
            for (Vector2d elementPosition : elements.keySet()) {
                lowerLeft = lowerLeft.lowerLeft(elementPosition);
                upperRight = upperRight.upperRight(elementPosition);
            }

        return mapVisualizer.draw(lowerLeft, upperRight);*/
    }
}
