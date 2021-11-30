package agh.ics.oop;

import java.util.*;

public class GrassField extends AbstractWorldMap {
    public GrassField(int grassCount) {
        super();

        Random random = new Random();
        int maxBound = (int)Math.ceil(Math.sqrt(grassCount * 10));

        for (int i = 0; i < grassCount; i++) {
            Vector2d nextGrassPosition;
            do {
                nextGrassPosition = new Vector2d(random.nextInt(maxBound), random.nextInt(maxBound));
            } while (isOccupied(nextGrassPosition));
            place(new Grass(nextGrassPosition));
        }
    }

    @Override
    public boolean isAccessible(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        Object objectAtPosition = objectAt(position);
        return objectAtPosition == null || objectAtPosition.getClass() == Grass.class;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        AbstractWorldMapElement animal = objectOfClassAt(Animal.class, position);
        if (animal != null)
            return animal;

        return objectOfClassAt(Grass.class, position);
    }
}
