package agh.ics.oop.observers;

public interface IOnNewEpochObservable {
    void addObserver(IOnNewEpochInvokeObserver observer);
    void removeObserver(IOnNewEpochInvokeObserver observer);
}
