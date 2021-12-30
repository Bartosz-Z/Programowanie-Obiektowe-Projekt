package agh.ics.oop.observers;

import agh.ics.oop.structures.EpochEndInfo;

public interface IOnEpochEndInvokeGUIObserver {
    void epochEnd(EpochEndInfo epochInfo);
}
