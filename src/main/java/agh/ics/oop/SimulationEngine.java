package agh.ics.oop;

import agh.ics.oop.observers.IOnDestroyInvokeObserver;
import agh.ics.oop.observers.IOnEpochEndInvokeObserver;
import agh.ics.oop.observers.IOnEpochEndObservable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements Runnable, IOnEpochEndObservable {
    private final AbstractJungleMap map;
    private final List<Animal> animals;
    private final WorldMapElementsStorage elementsStorage;
    private final int animalInitialEnergy, animalMaxEnergy, grassEnergy;
    private int magicEvolutionsLeft;

    private final List<IOnEpochEndInvokeObserver> epochEndObservers;

    public SimulationEngine(
            AbstractJungleMap map,
            int animalsCount,
            int animalInitialEnergy,
            int animalMaximumEnergy,
            int grassEnergy,
            boolean isMagicEvolution) {
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
        magicEvolutionsLeft = isMagicEvolution ? 3 : 0;
        epochEndObservers = new LinkedList<>();

        for (int i = 0; i < animalsCount; i++)
            placeAnimalInMap(getRandomUnoccupiedPosition(), this.animalInitialEnergy, getRandomGenome());
    }

    private void placeAnimalInMap(Vector2d position, int energy, Genome genome) {
        Animal animal = (Animal)elementsStorage.restore(Animal.class);
        if (animal == null) {
            animal = new Animal(map, position, energy, animalMaxEnergy, genome);
            animal.addObserver((IOnDestroyInvokeObserver) map);
        } else
            animal.updateState(position, energy, genome);

        map.place(animal);
        animals.add(animal);
    }

    private boolean isAnyUnoccupiedPosition() {
        int mapWidth = map.size.x();
        int mapHeight = map.size.y();

        boolean thereIsUnoccupiedTile = false;
        for (int row = 0; row < mapHeight; row++)
            for (int col = 0; col < mapWidth; col++)
                if (!map.isOccupied(new Vector2d(col, row))) {
                    thereIsUnoccupiedTile = true;
                    break;
                }

        return thereIsUnoccupiedTile;
    }

    private Vector2d getRandomUnoccupiedPosition() {
        return getRandomUnoccupiedPosition(
                Vector2d.zero,
                new Vector2d(map.size.x() - 1, map.size.y() - 1));
    }

    private Vector2d getRandomUnoccupiedPosition(Vector2d lowerLeftCorner, Vector2d upperRightCorner) {
        if (!lowerLeftCorner.follows(new Vector2d(0,0)) ||
                !lowerLeftCorner.precedes(new Vector2d(map.size.x() - 1, map.size.y() -1 )))
            throw new IllegalArgumentException("'lowerLeftCorner' need to be within map");

        if (!upperRightCorner.follows(new Vector2d(0,0)) ||
                !upperRightCorner.precedes(new Vector2d(map.size.x() - 1, map.size.y() -1 )))
            throw new IllegalArgumentException("'upperRightCorner' need to be within map");

        if (!lowerLeftCorner.precedes(upperRightCorner))
            throw new IllegalArgumentException("'lowerLeftCorner' should follows 'upperRightCorner'");

        int width = upperRightCorner.x() - lowerLeftCorner.x() + 1;
        int height = upperRightCorner.y() - lowerLeftCorner.y() + 1;

        Random random = ThreadLocalRandom.current();

        Vector2d position;
        int iteration = 0;
        int iterationToCheckIfMapIsFull = Math.round(width * height * 0.975f);
        boolean checkedIfMapIsFull = false;
        while (true) {
            if (!checkedIfMapIsFull) {
                if (iteration > iterationToCheckIfMapIsFull) {
                    checkedIfMapIsFull = true;
                    if (!isAnyUnoccupiedPosition())
                        return null;
                } else
                    iteration++;
            }
            position = new Vector2d(random.nextInt(width), random.nextInt(height));
            if (!map.isOccupied(position))
                return position;
        }
    }

    private Genome getRandomGenome() {
        return new Genome(ThreadLocalRandom.current().ints(32, 0, 8).toArray());
    }

    private void activateMagicEvolution() {
        magicEvolutionsLeft--;
        if (animals.size() == 0) {
            for (int i = 0; i < 5; i++)
                placeAnimalInMap(getRandomUnoccupiedPosition(), this.animalInitialEnergy, getRandomGenome());
        } else {
            int animalsCount = animals.size();
            Random random = ThreadLocalRandom.current();
            for (int i = 0; i < animalsCount; i++)
                placeAnimalInMap(
                        getRandomUnoccupiedPosition(),
                        this.animalInitialEnergy,
                        animals.get(random.nextInt(animalsCount)).getGenome());
        }
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
        if (animals.size() <= 5 && magicEvolutionsLeft > 0)
            activateMagicEvolution();
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
                map.remove(grass);
                elementsStorage.store(grass);
            }
        }
    }

    public void addNewGrasses() {
        Vector2d grassPosition = getRandomUnoccupiedPosition();
        if (grassPosition != null) {
            Grass grass = (Grass)elementsStorage.restore(Grass.class);
            if (grass == null)
                grass = new Grass(grassPosition, grassEnergy);
            else
                grass.updateState(grassPosition);
            map.place(grass);
        }
    }

    private void announceEpochEnd() {
        for (IOnEpochEndInvokeObserver observer : epochEndObservers)
            observer.epochEnd();
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        announceEpochEnd();
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            destroyDeadAnimals();
            moveAllAnimals();
            eatGrasses();
            for (int j = 0; j < 50; j++)
                addNewGrasses();
            announceEpochEnd();
        }
        System.out.println(System.currentTimeMillis() - timer);
    }

    @Override
    public void addObserver(IOnEpochEndInvokeObserver observer) {
        epochEndObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnEpochEndInvokeObserver observer) {
        epochEndObservers.remove(observer);
    }
}
