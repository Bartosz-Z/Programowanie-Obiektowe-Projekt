package agh.ics.oop;

public class Animal {
    private MapDirection mapDirection = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2, 2);

    @Override
    public String toString() {
        return position.toString() + " " + mapDirection.toString();
    }

    public boolean isAt(Vector2d position) {
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
                if (newPosition.follows(new Vector2d(0, 0)) && newPosition.precedes(new Vector2d(4, 4)))
                    position = newPosition;
            }
            case BACKWARD -> {
                Vector2d newPosition = position.subtract(mapDirection.toUnitVector());
                if (newPosition.follows(new Vector2d(0, 0)) && newPosition.precedes(new Vector2d(4, 4)))
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
