package agh.ics.oop;

public interface IPositionObservable {
    void addObserver(IPositionChangeObserver observer);
    void removeObserver(IPositionChangeObserver observer);
}
