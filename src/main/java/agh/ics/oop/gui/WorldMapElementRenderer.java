package agh.ics.oop.gui;

import agh.ics.oop.*;

public class WorldMapElementRenderer implements IPositionChangeObserver, IDirectionChangeObserver {
    private final AbstractWorldMapElement worldMapElement;

    public WorldMapElementRenderer(AbstractWorldMapElement worldMapElement) {
        this.worldMapElement = worldMapElement;

        if (worldMapElement instanceof IPositionObservable)
            ((IPositionObservable)worldMapElement).addObserver(this);
        if (worldMapElement instanceof IDirectionObservable)
            ((IDirectionObservable)worldMapElement).addObserver(this);
    }


    @Override
    public void directionChanged(AbstractWorldMapDynamicElement element) {

    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {

    }
}
