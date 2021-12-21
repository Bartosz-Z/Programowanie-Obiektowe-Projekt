package agh.ics.oop.observers;

public interface IOnEpochEndObservable {
    void addObserver(IOnEpochEndInvokeObserver observer);
    void removeObserver(IOnEpochEndInvokeObserver observer);
}
