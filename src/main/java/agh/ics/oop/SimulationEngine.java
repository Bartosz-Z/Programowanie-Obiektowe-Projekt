package agh.ics.oop;

import agh.ics.oop.observers.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

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
        Ensure.Not.Null(map, "world map");
        Ensure.Not.LessThen(animalsCount, 1, "animals count");
        Ensure.Is.MoreThen(animalInitialEnergy, 0, "animal's initial energy");
        Ensure.Is.MoreThen(animalMaximumEnergy, 0, "animal's maximum energy");
        Ensure.Is.MoreThen(grassEnergy, 0, "grass's energy");
        Ensure.Not.MoreThen(animalsCount, map.size.x() * map.size.y(),
                "animals count", "tiles in world map");

        this.map = map;

        animals = new LinkedList<>();
        elementsStorage = new WorldMapElementsStorage();
        this.animalInitialEnergy = animalInitialEnergy;
        animalMaxEnergy = animalMaximumEnergy;
        this.grassEnergy = grassEnergy;
        magicEvolutionsLeft = isMagicEvolution ? 3 : 0;
        epochEndObservers = new LinkedList<>();

        for (int i = 0; i < animalsCount; i++)
            placeAnimalInMap(map.getRandomUnoccupiedPosition(), this.animalInitialEnergy, new Genome());
    }

    private void placeAnimalInMap(Vector2d position, int energy, Genome genome) {
        Animal animal = (Animal) elementsStorage.restore(Animal.class);
        if (animal == null) {
            animal = new Animal(map, position, energy, animalMaxEnergy, genome);
            animal.addObserver((IOnDestroyInvokeObserver) map);
        } else
            animal.updateState(position, energy, genome);

        map.place(animal);
        animals.add(animal);
    }

    private void activateMagicEvolution() {
        magicEvolutionsLeft--;
        Supplier<Genome> getGenome;

        if (animals.size() == 0) {
            getGenome = Genome::new;
        } else {
            Random random = ThreadLocalRandom.current();
            int animalsCount = animals.size();
            getGenome = () -> animals.get(random.nextInt(animalsCount)).getGenome();
        }

        for (int i = 0; i < 5; i++) {
            Vector2d animalPosition = map.getRandomUnoccupiedPosition();
            if (animalPosition != null)
                placeAnimalInMap(animalPosition, animalInitialEnergy, getGenome.get());
            else
                break;
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

    public void addNewGrassInsideJungle() {
        Vector2d grassPosition = map.getRandomUnoccupiedPosition(
                map.jungleLowerLeftCorner,
                map.jungleUpperRightCorner);
        if (grassPosition != null) {
            Grass grass = elementsStorage.restore(Grass.class);
            if (grass == null)
                grass = new Grass(grassPosition, grassEnergy);
            else
                grass.updateState(grassPosition);
            map.place(grass);
        }
    }

    public void addNewGrassOutsideJungle() {
        Vector2d grassPosition = map.getRandomUnoccupiedPositionReversed(
                map.jungleLowerLeftCorner,
                map.jungleUpperRightCorner);
        if (grassPosition != null) {
            Grass grass = elementsStorage.restore(Grass.class);
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
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            destroyDeadAnimals();
            moveAllAnimals();
            eatGrasses();
            for (int j = 0; j < 1; j++) {
                addNewGrassInsideJungle();
                addNewGrassOutsideJungle();
            }
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
