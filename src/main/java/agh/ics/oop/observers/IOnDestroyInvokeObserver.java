package agh.ics.oop.observers;

import agh.ics.oop.elements.AbstractWorldMapElement;

public interface IOnDestroyInvokeObserver {
    void onElementDestroy(AbstractWorldMapElement element);
}
