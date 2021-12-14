package agh.ics.oop.observers;

public interface IOnDestroyObservable {
    void addObserver(IOnDestroyInvokeObserver observer);
    void removeObserver(IOnDestroyInvokeObserver observer);
}
