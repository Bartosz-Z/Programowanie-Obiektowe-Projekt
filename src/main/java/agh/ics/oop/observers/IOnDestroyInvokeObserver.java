package agh.ics.oop.observers;

import agh.ics.oop.AbstractWorldMapElement;

public interface IOnDestroyInvokeObserver {
    void onElementDestroy(AbstractWorldMapElement element);
}
