package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap {
    protected Map<Class<? extends AbstractWorldMapElement>, List<AbstractWorldMapElement>> worldMapElements;
    private final MapVisualizer mapVisualizer;

    protected AbstractWorldMap() {
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

        for (List<AbstractWorldMapElement> elements : worldMapElements.values())
            for (AbstractWorldMapElement element : elements)
                if (element.isAt(position))
                    return element;
        return null;
    }

    public <T extends AbstractWorldMapElement> boolean place(T element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        List<AbstractWorldMapElement> elements =
                worldMapElements.computeIfAbsent(element.getClass(), k -> new LinkedList<>());

        if (!isAccessible(element.getPosition()))
            return false;
        return elements.add(element);
    }

    @Override
    @Deprecated
    public boolean place(Animal animal) {
        return this.place((AbstractWorldMapElement) animal);
    }

    public <T extends AbstractWorldMapElement>
    List<? extends AbstractWorldMapElement> getWorldMapElements(Class<T> elementClass) {
        return worldMapElements.get(elementClass);
    }

    @Override
    public String toString() {
        Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (List<AbstractWorldMapElement> elements : worldMapElements.values())
            for (AbstractWorldMapElement element : elements) {
                lowerLeft = lowerLeft.lowerLeft(element.getPosition());
                upperRight = upperRight.upperRight(element.getPosition());
            }

        return mapVisualizer.draw(lowerLeft, upperRight);
    }
}
