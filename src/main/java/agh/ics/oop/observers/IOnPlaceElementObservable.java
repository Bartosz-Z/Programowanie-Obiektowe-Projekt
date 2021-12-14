package agh.ics.oop.observers;

public interface IOnPlaceElementObservable {
    void addObserver(IOnPlaceElementInvokeObserver observer);
    void removeObserver(IOnPlaceElementInvokeObserver observer);
}
