package agh.ics.oop;

import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import java.util.Comparator;

public class MapBoundary implements IPositionChangeObserver {
    private final SortedMultiset<Integer> xOrder, yOrder;

    public MapBoundary() {
        xOrder = TreeMultiset.create(Comparator.comparingInt(Integer::intValue));
        yOrder = TreeMultiset.create(Comparator.comparingInt(Integer::intValue));
    }

    public Vector2d getUpperRight() {
        Multiset.Entry<Integer> xEntry = xOrder.lastEntry();
        Multiset.Entry<Integer> yEntry = yOrder.lastEntry();
        if (xEntry == null || yEntry == null)
            return null;
        return new Vector2d(xEntry.getElement(), yEntry.getElement());
    }

    public Vector2d getLowerLeft() {
        Multiset.Entry<Integer> xEntry = xOrder.firstEntry();
        Multiset.Entry<Integer> yEntry = yOrder.firstEntry();
        if (xEntry == null || yEntry == null)
            return null;
        return new Vector2d(xEntry.getElement(), yEntry.getElement());
    }

    public void addPosition(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        xOrder.add(position.x());
        yOrder.add(position.y());
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        if (oldPosition == null)
            throw new IllegalArgumentException("'oldPosition' argument can not be null.");
        if (newPosition == null)
            throw new IllegalArgumentException("'newPosition' argument can not be null.");

        if (!xOrder.remove(oldPosition.x()) || !yOrder.remove(oldPosition.y()))
            throw new IllegalArgumentException("Vector2d: [" + oldPosition + "] was not found in multiset");

        xOrder.add(newPosition.x());
        yOrder.add(newPosition.y());
    }
}
