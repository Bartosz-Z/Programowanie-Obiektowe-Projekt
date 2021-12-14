package agh.ics.oop.observers;

import agh.ics.oop.AbstractWorldMapDynamicElement;

public interface IDirectionChangeObserver {
    void directionChanged(AbstractWorldMapDynamicElement element);
}
