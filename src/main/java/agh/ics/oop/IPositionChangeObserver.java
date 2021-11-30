package agh.ics.oop;

public interface IPositionChangeObserver {
    void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition);
}
