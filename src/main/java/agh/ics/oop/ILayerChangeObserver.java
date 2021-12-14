package agh.ics.oop;

public interface ILayerChangeObserver {
    void preLayerChanged(AbstractWorldMapElement element);
    void postLayerChanged(AbstractWorldMapElement element);
}
