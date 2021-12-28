package agh.ics.oop.observers;

import agh.ics.oop.elements.AbstractWorldMapDynamicElement;
import agh.ics.oop.structures.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(AbstractWorldMapDynamicElement element, Vector2d oldPosition, Vector2d newPosition);
}
