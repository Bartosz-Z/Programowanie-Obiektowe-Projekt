package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap implements IWorldMap {
    private final int height, width;
    private final List<Animal> animals;
    private final MapVisualizer mapVisualizer;

    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
        animals = new ArrayList<>();
        mapVisualizer = new MapVisualizer(this);
    }

    @Override
    public boolean isAccessible(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        if (!(position.follows(new Vector2d(0, 0)) && position.precedes(new Vector2d(width, height))))
            return false;
        return !isOccupied(position);
    }

    @Override
    public boolean place(Animal animal) {
        if (animal == null)
            throw new IllegalArgumentException("'animal' argument can not be null.");

        if (!isAccessible(animal.getPosition()))
            return false;
        animals.add(animal);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        for (Animal animal : animals)
            if (animal.isAt(position))
                return true;
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        for (Animal animal : animals)
            if (animal.isAt(position))
                return animal;
        return null;
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(new Vector2d(0, 0), new Vector2d(width, height));
    }
}
