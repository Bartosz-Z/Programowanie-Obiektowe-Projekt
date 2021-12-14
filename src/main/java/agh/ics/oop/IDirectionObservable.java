package agh.ics.oop;

public interface IDirectionObservable {
    void addObserver(IDirectionChangeObserver observer);
    void removeObserver(IDirectionChangeObserver observer);
}
