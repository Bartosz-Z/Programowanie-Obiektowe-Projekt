package agh.ics.oop.observers;

import agh.ics.oop.AbstractWorldMapElement;
import agh.ics.oop.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition);
}
