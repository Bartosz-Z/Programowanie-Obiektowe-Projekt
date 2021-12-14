package agh.ics.oop;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements Runnable {
    private final AbstractJungleMap map;
    private final List<Animal> animals;
    private final WorldMapElementsStorage elementsStorage;
    private final int animalInitialEnergy, grassEnergy;
    private int elementsOnMapCount;

    public SimulationEngine(
            AbstractJungleMap map,
            int animalsCount,
            int animalEnergy,
            int grassEnergy) {
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null");
        if (animalsCount < 2)
            throw new IllegalArgumentException("'animalsCount' argument should be at least 2");
        if (animalEnergy <= 0)
            throw new IllegalArgumentException("'animalEnergy' argument should be positive");
        if (grassEnergy <= 0)
            throw new IllegalArgumentException("'grassEnergy' argument should be positive");
        if (animalsCount > map.size.x() * map.size.y())
            throw new IllegalArgumentException("'animalsCount' argument should be equal or less then fields in map");

        this.map = map;
        animals = new LinkedList<>();
        elementsStorage = new WorldMapElementsStorage();
        animalInitialEnergy = animalEnergy;
        this.grassEnergy = grassEnergy;
        elementsOnMapCount = animalsCount;

        for (int i = 0; i < animalsCount; i++)
            placeAnimalInMap(getRandomUnoccupiedPosition(), animalInitialEnergy, getRandomGenome());
    }

    private void placeAnimalInMap(Vector2d position, int energy, Genome genome) {
        Animal animal = (Animal)elementsStorage.restore(Animal.class);
        if (animal == null)
            animal = new Animal(map, position, energy, genome);
        else
            animal.updateState(position, energy, genome);

        map.place(animal);
        animals.add(animal);
    }

    private Vector2d getRandomUnoccupiedPosition() {
        int mapWidth = map.size.x();
        int mapHeight = map.size.y();

        if (elementsOnMapCount >= mapWidth * mapHeight)
            return null;

        Random random = ThreadLocalRandom.current();

        Vector2d position;
        while (true) {
            position = new Vector2d(random.nextInt(mapWidth), random.nextInt(mapHeight));
            if (!map.isOccupied(position))
                return position;
        }
    }

    private Genome getRandomGenome() {
        return new Genome(ThreadLocalRandom.current().ints(32, 0, 8).toArray());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Animal animal : animals)
                animal.move();
        }


    }
}
