package agh.ics.oop.observers;

import agh.ics.oop.AbstractWorldMapDynamicElement;

public interface IOnDestroyInvokeObserver {
    void onElementDestroy(AbstractWorldMapDynamicElement element);
}
