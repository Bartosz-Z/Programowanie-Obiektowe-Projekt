package agh.ics.oop.observers;

public interface IPositionObservable {
    void addObserver(IPositionChangeObserver observer);
    void removeObserver(IPositionChangeObserver observer);
}
