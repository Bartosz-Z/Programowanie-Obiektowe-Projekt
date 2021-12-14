package agh.ics.oop;

public interface IOnDestroyObservable {
    void addObserver(IOnDestroyInvokeObserver observer);
    void removeObserver(IOnDestroyInvokeObserver observer);
}
