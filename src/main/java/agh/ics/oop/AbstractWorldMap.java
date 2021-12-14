package agh.ics.oop;

import java.util.*;

public abstract class AbstractWorldMap
        implements IWorldMap, IPositionChangeObserver, ILayerChangeObserver, IOnDestroyInvokeObserver {
    protected final Map<Vector2d, SortedSet<AbstractWorldMapElement>> worldMapElements;
    private final Comparator<AbstractWorldMapElement> comparator;
    public final Vector2d size;

    protected AbstractWorldMap(Vector2d mapSize) {
        if (mapSize == null)
            throw new IllegalArgumentException("'mapSize' argument can not be null.");
        if (mapSize.x() <= 0)
            throw new IllegalArgumentException("Map width must be positive.");
        if (mapSize.y() <= 0)
            throw new IllegalArgumentException("Map height must be positive.");

        worldMapElements = new HashMap<>();
        comparator = (element1, element2) -> {
            if (element1.getLayer() == element2.getLayer())
                return element1.hashCode() - element2.hashCode();
            return element1.getLayer() - element2.getLayer();
        };
        size = mapSize;
    }

    @Override
    public abstract boolean isAccessible(Vector2d position);

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        SortedSet<AbstractWorldMapElement> elements = worldMapElements.get(position);
        if (elements == null)
            return null;
        try {
            return elements.first();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public void place(AbstractWorldMapElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        Vector2d elementPosition = element.getPosition();

        if (!isAccessible(elementPosition))
            throw new IllegalArgumentException("Cannot place place element at not accessible field.");

        worldMapElements.computeIfAbsent(elementPosition, k -> new TreeSet<>(comparator)).add(element);

        if (element instanceof IPositionObservable)
            ((IPositionObservable) element).addObserver(this);
        if (element instanceof ILayerObservable)
            ((ILayerObservable) element).addObserver(this);
    }

    public void remove(AbstractWorldMapElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        SortedSet<AbstractWorldMapElement> elements = worldMapElements.get(element.getPosition());
        if (elements == null || !elements.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");
        if (oldPosition == null)
            throw new IllegalArgumentException("'oldPosition' argument can not be null.");
        if (newPosition == null)
            throw new IllegalArgumentException("'newPosition' argument can not be null.");

        SortedSet<AbstractWorldMapElement> oldElements = worldMapElements.get(oldPosition);
        if (oldElements == null || !oldElements.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");

        worldMapElements.computeIfAbsent(newPosition, k -> new TreeSet<>(comparator)).add(element);
    }

    @Override
    public void preLayerChanged(AbstractWorldMapElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        SortedSet<AbstractWorldMapElement> elementsOnSameTile = worldMapElements.get(element.position);
        if (elementsOnSameTile == null || !elementsOnSameTile.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    @Override
    public void postLayerChanged(AbstractWorldMapElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        SortedSet<AbstractWorldMapElement> elementsOnSameTile = worldMapElements.get(element.position);
        if (elementsOnSameTile == null || !elementsOnSameTile.add(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    @Override
    public void onElementDestroy(AbstractWorldMapDynamicElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        SortedSet<AbstractWorldMapElement> elementsOnSameTile = worldMapElements.get(element.position);
        if (elementsOnSameTile == null || !elementsOnSameTile.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    public abstract ImageName getImageNameOfTile(Vector2d tilePosition);
}
