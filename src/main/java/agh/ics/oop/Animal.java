package agh.ics.oop;

public class Animal {
    public final IWorldMap map;
    private MapDirection mapDirection = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2, 2);

    public Animal(IWorldMap map) {
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");

        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        this(map);
        if (initialPosition == null)
            throw new IllegalArgumentException("'initialPosition' argument can not be null.");

        position = initialPosition;
    }

    @Override
    public String toString() {
        return Character.toString(mapDirection.toString().charAt(0));
    }

    public boolean isAt(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        return this.position.equals(position);
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

    public Vector2d getPosition() {
        return position;
    }
}
