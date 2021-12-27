package agh.ics.oop.observers;

import agh.ics.oop.elements.AbstractWorldMapElement;

public interface ILayerChangeObserver {
    void preLayerChanged(AbstractWorldMapElement element);
    void postLayerChanged(AbstractWorldMapElement element);
}
