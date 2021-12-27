package agh.ics.oop;

import agh.ics.oop.observers.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public abstract class AbstractWorldMap
        implements
        IWorldMap,
        IPositionChangeObserver,
        ILayerChangeObserver,
        IOnDestroyInvokeObserver,
        IOnPlaceElementObservable {
    protected final Map<Vector2d, SortedSet<AbstractWorldMapElement>> worldMapElements;
    private final Comparator<AbstractWorldMapElement> comparator;
    public final Vector2d size;
    private final List<IOnPlaceElementInvokeObserver> onPlaceObservers;

    protected AbstractWorldMap(Vector2d mapSize) {
        Ensure.Not.Null(mapSize, "size of world map");
        Ensure.Is.MoreThen(mapSize.x(), 0, "world map width");
        Ensure.Is.MoreThen(mapSize.y(), 0, "world map height");

        worldMapElements = new HashMap<>();
        comparator = (element1, element2) -> {
            if (element1.getLayer() == element2.getLayer())
                return element1.hashCode() - element2.hashCode();
            return element2.getLayer() - element1.getLayer();
        };
        size = mapSize;
        onPlaceObservers = new LinkedList<>();
    }

    @Override
    public abstract boolean isAccessible(Vector2d position);

    @Override
    public boolean isOccupied(Vector2d position) {
        return firstObjectAt(position) != null;
    }

    public boolean isAnyUnoccupiedPosition() {
        return isAnyUnoccupiedPosition(Vector2d.zero, new Vector2d(size.x() - 1, size.y() - 1));
    }

    public boolean isAnyUnoccupiedPosition(Vector2d lowerLeftCorner, Vector2d upperRightCorner) {
        if (!lowerLeftCorner.follows(Vector2d.zero) ||
                !lowerLeftCorner.precedes(new Vector2d(size.x() - 1, size.y() -1 )))
            throw new IllegalArgumentException("'lowerLeftCorner' need to be within map");

        if (!upperRightCorner.follows(Vector2d.zero) ||
                !upperRightCorner.precedes(new Vector2d(size.x() - 1, size.y() -1 )))
            throw new IllegalArgumentException("'upperRightCorner' need to be within map");

        if (!lowerLeftCorner.precedes(upperRightCorner))
            throw new IllegalArgumentException("'lowerLeftCorner' should follows 'upperRightCorner'");

        for (int row = lowerLeftCorner.y(); row <= upperRightCorner.y(); row++)
            for (int col = lowerLeftCorner.x(); col <= upperRightCorner.x(); col++)
                if (!isOccupied(new Vector2d(col, row)))
                    return true;
        return false;
    }

    public boolean isAnyUnoccupiedPositionReversed(Vector2d lowerLeftCorner, Vector2d upperRightCorner) {
        if (!lowerLeftCorner.follows(Vector2d.zero) ||
                !lowerLeftCorner.precedes(new Vector2d(size.x() - 1, size.y() -1 )))
            throw new IllegalArgumentException("'lowerLeftCorner' need to be within map");

        if (!upperRightCorner.follows(Vector2d.zero) ||
                !upperRightCorner.precedes(new Vector2d(size.x() - 1, size.y() -1 )))
            throw new IllegalArgumentException("'upperRightCorner' need to be within map");

        if (!lowerLeftCorner.precedes(upperRightCorner))
            throw new IllegalArgumentException("'lowerLeftCorner' should follows 'upperRightCorner'");

        for (int row = 0; row < size.y(); row++)
            for (int col = 0; col < lowerLeftCorner.x(); col++)
                if (!isOccupied(new Vector2d(col, row)))
                    return true;

        for (int row = 0; row < size.y(); row++)
            for (int col = upperRightCorner.x() + 1; col < size.x(); col++)
                if (!isOccupied(new Vector2d(col, row)))
                    return true;

        for (int row = 0; row < lowerLeftCorner.y(); row++)
            for (int col = lowerLeftCorner.x(); col <= upperRightCorner.x(); col++)
                if (!isOccupied(new Vector2d(col, row)))
                    return true;

        for (int row = upperRightCorner.y() + 1; row < size.y(); row++)
            for (int col = lowerLeftCorner.x(); col <= upperRightCorner.x(); col++)
                if (!isOccupied(new Vector2d(col, row)))
                    return true;

        return false;
    }

    public Vector2d getRandomUnoccupiedPosition() {
        return getRandomUnoccupiedPosition(Vector2d.zero, new Vector2d(size.x() - 1, size.y() - 1));
    }

    public Vector2d getRandomUnoccupiedPosition(Vector2d lowerLeftCorner, Vector2d upperRightCorner) {
        return getRandomUnoccupiedPosition(lowerLeftCorner, upperRightCorner,
                position -> !isOccupied(position),
                a -> b -> () -> isAnyUnoccupiedPosition(a, b));
    }

    public Vector2d getRandomUnoccupiedPositionReversed(Vector2d lowerLeftCorner, Vector2d upperRightCorner) {
        return getRandomUnoccupiedPosition(Vector2d.zero, new Vector2d(size.x() - 1, size.y() - 1),
                position -> !isOccupied(position) && !(position.follows(lowerLeftCorner) && position.precedes(upperRightCorner)),
                a -> b -> () -> isAnyUnoccupiedPositionReversed(a, b));
    }

    protected Vector2d getRandomUnoccupiedPosition(
            Vector2d lowerLeftCorner,
            Vector2d upperRightCorner,
            Function<Vector2d, Boolean> isPositionCorrect,
            Function<Vector2d, Function<Vector2d, BooleanSupplier>> anyUnoccupiedCondition) {
        if (!lowerLeftCorner.follows(new Vector2d(0,0)) ||
                !lowerLeftCorner.precedes(new Vector2d(size.x() - 1, size.y() -1 )))
            throw new IllegalArgumentException("'lowerLeftCorner' need to be within map");

        if (!upperRightCorner.follows(new Vector2d(0,0)) ||
                !upperRightCorner.precedes(new Vector2d(size.x() - 1, size.y() -1 )))
            throw new IllegalArgumentException("'upperRightCorner' need to be within map");

        if (!lowerLeftCorner.precedes(upperRightCorner))
            throw new IllegalArgumentException("'lowerLeftCorner' should follows 'upperRightCorner'");

        int width = upperRightCorner.x() - lowerLeftCorner.x() + 1;
        int height = upperRightCorner.y() - lowerLeftCorner.y() + 1;

        Random random = ThreadLocalRandom.current();

        Vector2d position;
        int iteration = 0;
        int iterationToCheckIfMapIsFull = Math.round(width * height * 0.975f);
        boolean checkedIfMapIsFull = false;
        while (true) {
            if (!checkedIfMapIsFull) {
                if (iteration > iterationToCheckIfMapIsFull) {
                    checkedIfMapIsFull = true;
                    if (!anyUnoccupiedCondition.apply(lowerLeftCorner).apply(upperRightCorner).getAsBoolean())
                        return null;
                } else
                    iteration++;
            }
            position = new Vector2d(
                    lowerLeftCorner.x() + random.nextInt(width),
                    lowerLeftCorner.y() + random.nextInt(height));
            if (isPositionCorrect.apply(position))
                return position;
        }
    }

    @Override
    public AbstractWorldMapElement firstObjectAt(Vector2d position) {
        Ensure.Not.Null(position, "position");

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
    public AbstractWorldMapElement lastObjectAt(Vector2d position) {
        Ensure.Not.Null(position, "position");

        SortedSet<AbstractWorldMapElement> elements = worldMapElements.get(position);
        if (elements == null)
            return null;
        try {
            return elements.last();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public SortedSet<? extends AbstractWorldMapElement> getElementsOnPosition(Vector2d position) {
        return worldMapElements.get(position);
    }

    @Override
    public void place(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element");

        Vector2d elementPosition = element.getPosition();

        if (!isAccessible(elementPosition))
            throw new IllegalArgumentException("Cannot place place element at not accessible tile.");

        worldMapElements.computeIfAbsent(elementPosition, k -> new TreeSet<>(comparator)).add(element);

        if (element instanceof IPositionObservable)
            ((IPositionObservable) element).addObserver(this);
        if (element instanceof ILayerObservable)
            ((ILayerObservable) element).addObserver(this);

        for (IOnPlaceElementInvokeObserver observer : onPlaceObservers)
            observer.elementPlaced(element);
    }

    public void remove(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element to remove");

        SortedSet<AbstractWorldMapElement> elements = worldMapElements.get(element.getPosition());
        if (elements == null || !elements.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        Ensure.Not.Null(element, "element");
        Ensure.Not.Null(oldPosition, "old element's position");
        Ensure.Not.Null(newPosition, "new element's position");

        SortedSet<AbstractWorldMapElement> oldElements = worldMapElements.get(oldPosition);
        if (oldElements == null || !oldElements.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");

        worldMapElements.computeIfAbsent(newPosition, k -> new TreeSet<>(comparator)).add(element);
    }

    @Override
    public void preLayerChanged(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element");

        SortedSet<AbstractWorldMapElement> elementsOnSameTile = worldMapElements.get(element.position);
        if (elementsOnSameTile == null || !elementsOnSameTile.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    @Override
    public void postLayerChanged(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element");

        SortedSet<AbstractWorldMapElement> elementsOnSameTile = worldMapElements.get(element.position);
        if (elementsOnSameTile == null || !elementsOnSameTile.add(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    @Override
    public void onElementDestroy(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element");

        SortedSet<AbstractWorldMapElement> elementsOnSameTile = worldMapElements.get(element.position);
        if (elementsOnSameTile == null || !elementsOnSameTile.remove(element))
            throw new IllegalStateException("State of element [" + element + "] in map is invalid.");
    }

    public abstract ImageName getImageNameOfTile(Vector2d tilePosition);

    @Override
    public void addObserver(IOnPlaceElementInvokeObserver observer) {
        onPlaceObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnPlaceElementInvokeObserver observer) {
        onPlaceObservers.remove(observer);
    }
}
