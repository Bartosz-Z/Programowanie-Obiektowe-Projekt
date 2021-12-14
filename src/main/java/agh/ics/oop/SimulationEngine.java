package agh.ics.oop;

import agh.ics.oop.observers.IOnDestroyInvokeObserver;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements Runnable {
    private final AbstractJungleMap map;
    private final List<Animal> animals;
    private final WorldMapElementsStorage elementsStorage;
    private final int animalInitialEnergy, animalMaxEnergy, grassEnergy;

    public SimulationEngine(
            AbstractJungleMap map,
            int animalsCount,
            int animalInitialEnergy,
            int animalMaximumEnergy,
            int grassEnergy) {
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null");
        if (animalsCount < 1)
            throw new IllegalArgumentException("'animalsCount' argument should be at least 1");
        if (animalInitialEnergy <= 0)
            throw new IllegalArgumentException("'animalEnergy' argument should be positive");
        if (animalMaximumEnergy <= 0)
            throw new IllegalArgumentException("'animalMaximumEnergy' argument should be positive");
        if (grassEnergy <= 0)
            throw new IllegalArgumentException("'grassEnergy' argument should be positive");
        if (animalsCount > map.size.x() * map.size.y())
            throw new IllegalArgumentException("'animalsCount' argument should be equal or less then fields in map");

        this.map = map;
        animals = new LinkedList<>();
        elementsStorage = new WorldMapElementsStorage();
        this.animalInitialEnergy = animalInitialEnergy;
        animalMaxEnergy = animalMaximumEnergy;
        this.grassEnergy = grassEnergy;

        for (int i = 0; i < animalsCount; i++)
            placeAnimalInMap(getRandomUnoccupiedPosition(), this.animalInitialEnergy, getRandomGenome());
    }

    private void placeAnimalInMap(Vector2d position, int energy, Genome genome) {
        Animal animal = (Animal)elementsStorage.restore(Animal.class);
        if (animal == null)
            animal = new Animal(map, position, energy, animalMaxEnergy, genome);
        else
            animal.updateState(position, energy, genome);

        animal.addObserver((IOnDestroyInvokeObserver) map);
        map.place(animal);
        animals.add(animal);
    }

    private Vector2d getRandomUnoccupiedPosition() {
        int mapWidth = map.size.x();
        int mapHeight = map.size.y();

        boolean thereIsUnoccupiedTile = false;
        for (int row = 0; row < mapHeight; row++)
            for (int col = 0; col < mapHeight; col++)
                if (!map.isOccupied(new Vector2d(col, row))) {
                    thereIsUnoccupiedTile = true;
                    break;
                }
        if (!thereIsUnoccupiedTile)
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

    private void destroyDeadAnimals() {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (!animal.isAlive()) {
                animal.destroy();
                iterator.remove();
            }
        }
    }

    private void moveAllAnimals() {
        animals.forEach(Animal::move);
    }

    private void eatGrasses() {
        for (Animal animal : animals) {
            SortedSet<? extends AbstractWorldMapElement> elementsOnSameTile
                    = map.getElementsOnPosition(animal.getPosition());
            if (elementsOnSameTile.last() instanceof Grass grass) {
                ArrayList<Animal> animalsOnSameTile = new ArrayList<>();
                for (AbstractWorldMapElement element : elementsOnSameTile) {
                    if (element instanceof Animal animalOnSameTile) {
                        if (animalsOnSameTile.size() == 0)
                            animalsOnSameTile.add(animalOnSameTile);
                        else if (animalsOnSameTile.get(0).getEnergy() == animalOnSameTile.getEnergy())
                            animalsOnSameTile.add(animalOnSameTile);
                        else
                            break;
                    }
                }
                for (Animal animalOnSameTile : animalsOnSameTile)
                    animalOnSameTile.addEnergy(grass.energy / animalsOnSameTile.size());
            }
        }
    }

    public void addNewGrasses() {
        Vector2d grassPosition = getRandomUnoccupiedPosition();
        if (grassPosition != null)
            map.place(new Grass(grassPosition, grassEnergy));
    }

    @Override
    public void run() {
        for (int i = 0; i < 300; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            destroyDeadAnimals();
            moveAllAnimals();
            eatGrasses();
            addNewGrasses();
        }
    }
}
