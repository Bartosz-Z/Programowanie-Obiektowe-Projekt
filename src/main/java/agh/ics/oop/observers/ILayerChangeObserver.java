package agh.ics.oop.observers;

import agh.ics.oop.AbstractWorldMapElement;

public interface ILayerChangeObserver {
    void preLayerChanged(AbstractWorldMapElement element);
    void postLayerChanged(AbstractWorldMapElement element);
}
