package agh.ics.oop;

public interface IObservable {
    void addObserver(IPositionChangeObserver observer);
    void removeObserver(IPositionChangeObserver observer);
}
