package agh.ics.oop.observers;

public interface IDirectionObservable {
    void addObserver(IDirectionChangeObserver observer);
    void removeObserver(IDirectionChangeObserver observer);
}
