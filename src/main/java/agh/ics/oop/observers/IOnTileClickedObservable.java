package agh.ics.oop.observers;

public interface IOnTileClickedObservable {
    void addObserver(IOnTileClickedEventObserver observer);
    void removeObserver(IOnTileClickedEventObserver observer);
}
