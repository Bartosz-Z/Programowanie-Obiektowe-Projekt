package agh.ics.oop;

import agh.ics.oop.elements.*;
import agh.ics.oop.maps.AbstractJungleMap;
import agh.ics.oop.observers.*;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimulationEngine implements Runnable, IOnDestroyInvokeObserver, IOnEpochEndObservable {
    private final AbstractJungleMap map;
    private final List<Animal> animals;
    private final List<Grass> grasses;
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
        grasses = new LinkedList<>();
        this.animalInitialEnergy = animalInitialEnergy;
        animalMaxEnergy = animalMaximumEnergy;
        this.grassEnergy = grassEnergy;
        magicEvolutionsLeft = isMagicEvolution ? 3 : 0;
        epochEndObservers = new LinkedList<>();

        for (int i = 0; i < animalsCount; i++)
            placeAnimalInMap(map.getRandomUnoccupiedPosition(), this.animalInitialEnergy, new Genome());
    }

    private void placeAnimalInMap(Vector2d position, int energy, Genome genome) {
        Animal animal = Animal.createAnimal(map, position, energy, animalMaxEnergy, genome);
        animal.addObserver((IOnDestroyInvokeObserver) map);

        map.place(animal);
        animals.add(animal);
    }

    private void placeGrassInMap(Vector2d position) {
        Grass grass = Grass.createGrass(position, grassEnergy);
        grass.addObserver(this);

        map.place(grass);
        grasses.add(grass);
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
        List<? extends AbstractWorldMapElement> elements;

        if (animals.size() > grasses.size())
            elements = grasses;
        else
            elements = animals;

        for (AbstractWorldMapElement element : elements) {
            SortedSet<? extends AbstractWorldMapElement> elementsOnSameTile
                    = map.getElementsOnPosition(element.getPosition());

            Grass grassOnTile = null;
            for (AbstractWorldMapElement elementOnSameTile : elementsOnSameTile)
                if (elementOnSameTile instanceof Grass) {
                    grassOnTile = (Grass) elementOnSameTile;
                    break;
                }

            if (grassOnTile != null) {
                ArrayList<Animal> animalsAllowedToEat = new ArrayList<>();
                for (AbstractWorldMapElement elementOnSameTile : elementsOnSameTile)
                    if (elementOnSameTile instanceof Animal animal &&
                            (animalsAllowedToEat.size() == 0 ||
                                    animal.getEnergy() == animalsAllowedToEat.get(0).getEnergy()))
                        animalsAllowedToEat.add(animal);
                    else
                        break;

                if (animalsAllowedToEat.size() > 0) {
                    for (Animal animalAllowedToEast : animalsAllowedToEat)
                        animalAllowedToEast.addEnergy(grassOnTile.energy / animalsAllowedToEat.size());
                    map.remove(grassOnTile);
                    WorldMapElementsStorage.store(grassOnTile);
                }
            }
        }
    }

    private void tryAddGrassInMap(Function<Vector2d, Function<Vector2d, Vector2d>> positionGetter) {
        Vector2d grassPosition = positionGetter.apply(map.jungleLowerLeftCorner).apply(map.jungleUpperRightCorner);
        if (grassPosition != null)
            placeGrassInMap(grassPosition);
    }

    private void makeCrossovers() {
        Set<Vector2d> positionsWithCrossover = new HashSet<>();
        List<Animal> animalsToBePlaced = new LinkedList<>();
        int minimalEnergy = animalMaxEnergy % 2 == 0 ? animalMaxEnergy / 2 : animalMaxEnergy / 2 + 1;

        for (Animal animal : animals) {
            if (!positionsWithCrossover.contains(animal.getPosition())) {
                SortedSet<? extends AbstractWorldMapElement> elementsOnSameTile
                        = map.getElementsOnPosition(animal.getPosition());

                LinkedList<Animal> animalsAllowedToCrossover = new LinkedList<>();
                for (AbstractWorldMapElement element : elementsOnSameTile) {
                    if (element instanceof Animal animalOnSameTile && animalOnSameTile.getEnergy() >= minimalEnergy) {
                        if (animalsAllowedToCrossover.size() == 0 ||
                                animalOnSameTile.getEnergy() == animalsAllowedToCrossover.getFirst().getEnergy() ||
                                animalsAllowedToCrossover.size() == 1 ||
                                animalOnSameTile.getEnergy() == animalsAllowedToCrossover.get(1).getEnergy())
                            animalsAllowedToCrossover.add(animalOnSameTile);
                        else
                            break;
                    } else
                        break;
                }

                if (animalsAllowedToCrossover.size() >= 2) {
                    Random random = ThreadLocalRandom.current();
                    positionsWithCrossover.add(animal.getPosition());

                    Animal firstAnimal = animalsAllowedToCrossover.get(random.nextInt(animalsAllowedToCrossover.size()));
                    animalsAllowedToCrossover.remove(firstAnimal);
                    Animal secondAnimal = animalsAllowedToCrossover.get(random.nextInt(animalsAllowedToCrossover.size()));

                    animalsToBePlaced.add(firstAnimal.crossoverWith(secondAnimal));
                }
            }
        }

        for (Animal animal : animalsToBePlaced) {
            animal.addObserver((IOnDestroyInvokeObserver) map);
            map.place(animal);
        }
        animals.addAll(animalsToBePlaced);
    }

    private void announceEpochEnd() {
        for (IOnEpochEndInvokeObserver observer : epochEndObservers)
            observer.epochEnd();
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        announceEpochEnd();
        for (int i = 0; i < 4000; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            destroyDeadAnimals();
            moveAllAnimals();
            eatGrasses();
            makeCrossovers();
            for (int j = 0; j < 1; j++) {
                tryAddGrassInMap(a -> b -> map.getRandomUnoccupiedPosition(a, b));
                tryAddGrassInMap(a -> b -> map.getRandomUnoccupiedPositionReversed(a, b));
            }
            announceEpochEnd();
        }
        System.out.println(System.currentTimeMillis() - timer);
        for (Animal animal : animals)
            System.out.println(animal.getGenome());
    }

    @Override
    public void addObserver(IOnEpochEndInvokeObserver observer) {
        epochEndObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnEpochEndInvokeObserver observer) {
        epochEndObservers.remove(observer);
    }

    @Override
    public void onElementDestroy(AbstractWorldMapElement element) {
        if (!(element instanceof Grass grass))
            throw new IllegalStateException("OnDestroy event of element [" + element + "] should not be observed.");

        if (!grasses.remove(grass))
            throw new IllegalStateException("Grass [" + grass + "] is not listed.");
    }
}
