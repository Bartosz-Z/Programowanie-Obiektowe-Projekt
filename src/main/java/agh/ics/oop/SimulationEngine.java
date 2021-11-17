package agh.ics.oop;

import java.util.Arrays;

public class SimulationEngine implements IEngine {
    private final MoveDirection[] moveDirectionsSequence;
    private final Animal[] animals;

    public SimulationEngine(IWorldMap map, MoveDirection[] moveDirections, Vector2d[] initialPositions) {
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null");
        if (moveDirections == null)
            throw new IllegalArgumentException("'moveDirections' argument can not be null");
        if (initialPositions == null)
            throw new IllegalArgumentException("'initialPositions' argument can not be null");

        animals = Arrays.stream(initialPositions)
                .map(position -> new Animal(map, position))
                .filter(map::place)
                .toArray(Animal[]::new);

        moveDirectionsSequence = moveDirections;
    }

    @Override
    public void run() {
        if (animals.length > 0)
            for (int i = 0; i < moveDirectionsSequence.length; i++)
                animals[i % animals.length].move(moveDirectionsSequence[i]);
    }
}
