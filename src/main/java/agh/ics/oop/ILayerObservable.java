package agh.ics.oop;

public interface ILayerObservable {
    void addObserver(ILayerChangeObserver observer);
    void removeObserver(ILayerChangeObserver observer);
}
