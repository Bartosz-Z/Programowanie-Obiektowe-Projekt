package agh.ics.oop.observers;

import agh.ics.oop.elements.AbstractWorldMapElement;
import agh.ics.oop.structures.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition);
}
