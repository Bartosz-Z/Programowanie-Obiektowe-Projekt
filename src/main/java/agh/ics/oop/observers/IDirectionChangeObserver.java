package agh.ics.oop.observers;

import agh.ics.oop.elements.AbstractWorldMapDynamicElement;

public interface IDirectionChangeObserver {
    void directionChanged(AbstractWorldMapDynamicElement element);
}
