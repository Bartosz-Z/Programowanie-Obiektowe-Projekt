package agh.ics.oop.observers;

public interface IOnEpochEndGUIObservable {
    void addObserver(IOnEpochEndInvokeGUIObserver observer);
    void removeObserver(IOnEpochEndInvokeGUIObserver observer);
}
