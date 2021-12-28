package agh.ics.oop.maps;

import agh.ics.oop.elements.AbstractWorldMapDynamicElement;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;

public class WrappedJungleMap extends AbstractJungleMap {

    public WrappedJungleMap(Vector2d size, float jungleRatio) {
        super(size, jungleRatio);
    }

    public WrappedJungleMap(int width, int height, float jungleRatio) {
        this(new Vector2d(width, height), jungleRatio);
    }

    @Override
    public boolean isAccessible(Vector2d position) {
        Ensure.Not.Null(position, "position");

        return position.follows(new Vector2d(-1, -1)) && position.precedes(size);
    }

    @Override
    public void positionChanged(AbstractWorldMapDynamicElement element, Vector2d oldPosition, Vector2d newPosition) {
        Vector2d wrappedPosition = new Vector2d(
                newPosition.x() == -1 ? size.x() - 1 : newPosition.x() % size.x(),
                newPosition.y() == -1 ? size.y() - 1 : newPosition.y() % size.y());

        super.positionChanged(element, oldPosition, newPosition);

        if (!wrappedPosition.equals(newPosition))
            element.tryChangePosition(wrappedPosition);
    }
}
