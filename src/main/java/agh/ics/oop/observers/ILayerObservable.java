package agh.ics.oop.observers;

public interface ILayerObservable {
    void addObserver(ILayerChangeObserver observer);
    void removeObserver(ILayerChangeObserver observer);
}
