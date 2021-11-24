package agh.ics.oop;

public class Animal extends AbstractWorldMapElement {
    public final IWorldMap map;
    private MapDirection mapDirection = MapDirection.NORTH;

    public Animal(IWorldMap map) {
        super(new Vector2d(2, 2));
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");

        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        super(initialPosition);
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");

        this.map = map;
    }

    @Override
    protected char getIcon() {
        return mapDirection.toString().charAt(0);
    }

    public void move(MoveDirection direction) {
        if (direction == null)
            throw new IllegalArgumentException("Argument can not be null.");

        switch (direction) {
            case RIGHT -> mapDirection = mapDirection.next();
            case LEFT -> mapDirection = mapDirection.previous();
            case FORWARD -> {
                Vector2d newPosition = position.add(mapDirection.toUnitVector());
                if (map.isAccessible(newPosition))
                    position = newPosition;
            }
            case BACKWARD -> {
                Vector2d newPosition = position.subtract(mapDirection.toUnitVector());
                if (map.isAccessible(newPosition))
                    position = newPosition;
            }
        }
    }

    public MapDirection getOrientation() {
        return mapDirection;
    }
}
