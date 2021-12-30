package agh.ics.oop;

import agh.ics.oop.elements.*;
import agh.ics.oop.maps.AbstractJungleMap;
import agh.ics.oop.observers.*;
import agh.ics.oop.structures.EpochEndInfo;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class SimulationEngine implements Runnable, IOnEpochEndObservable, IOnEpochEndGUIObservable {
    private final AbstractJungleMap map;
    private final List<Animal> animals;
    private final List<Grass> grasses;
    private final int
            animalInitialEnergy,
            animalMaxEnergy,
            animalForwardEnergyCost,
            animalBackwardEnergyCost,
            animalRotationEnergyCost,
            grassEnergy,
            timeDelay;
    private int magicEvolutionsLeft;

    private final List<IOnEpochEndInvokeObserver> epochEndObservers;
    private final List<IOnEpochEndInvokeGUIObserver> epochEndGUIObservers;
    private boolean isStopped, isAlive;

    private int animalsDead;
    private final List<Genome> dominantGenomes;
    private float averageAnimalsEnergy, averageAnimalAliveTime, averageAnimalsOffspringCount;

    public SimulationEngine(
            AbstractJungleMap map,
            int animalsCount,
            int animalInitialEnergy,
            int animalMaximumEnergy,
            int animalForwardEnergyCost,
            int animalBackwardEnergyCost,
            int animalRotationEnergyCost,
            int grassEnergy,
            int timeDelay,
            boolean isMagicEvolution) {
        Ensure.Not.Null(map, "world map");
        Ensure.Not.LessThen(animalsCount, 1, "animals count");
        Ensure.Is.MoreThen(animalInitialEnergy, 0, "animal's initial energy");
        Ensure.Is.MoreThen(animalMaximumEnergy, 0, "animal's maximum energy");
        Ensure.Is.MoreThen(animalForwardEnergyCost, 0, "animal's forward cost");
        Ensure.Is.MoreThen(animalBackwardEnergyCost, 0, "animal's backward cost");
        Ensure.Is.MoreThen(animalRotationEnergyCost, 0, "animal's rotation cost");
        Ensure.Is.MoreThen(grassEnergy, 0, "grass's energy");
        Ensure.Is.MoreThen(timeDelay, 0, "time delay between epochs");
        Ensure.Not.MoreThen(animalsCount, map.size.x() * map.size.y(),
                "animals count", "tiles in world map");

        this.map = map;

        animals = new LinkedList<>();
        grasses = new LinkedList<>();
        this.animalInitialEnergy = animalInitialEnergy;
        animalMaxEnergy = animalMaximumEnergy;
        this.animalForwardEnergyCost = animalForwardEnergyCost;
        this.animalBackwardEnergyCost = animalBackwardEnergyCost;
        this.animalRotationEnergyCost = animalRotationEnergyCost;
        this.grassEnergy = grassEnergy;
        this.timeDelay = timeDelay;
        magicEvolutionsLeft = isMagicEvolution ? 3 : 0;
        epochEndObservers = new LinkedList<>();
        epochEndGUIObservers = new LinkedList<>();
        isStopped = false;
        isAlive = true;

        dominantGenomes = new LinkedList<>();
        averageAnimalsEnergy = 0;
        averageAnimalAliveTime = 0;
        averageAnimalsOffspringCount = 0;
        animalsDead = 0;

        for (int i = 0; i < animalsCount; i++)
            placeAnimalInMap(map.getRandomUnoccupiedPosition(), this.animalInitialEnergy, new Genome());
    }

    private void placeAnimalInMap(Vector2d position, int energy, Genome genome) {
        Animal animal = Animal.createAnimal(
                map,
                position,
                energy,
                animalMaxEnergy,
                animalForwardEnergyCost,
                animalBackwardEnergyCost,
                animalRotationEnergyCost,
                genome);

        map.place(animal);
        animals.add(animal);
    }

    private void placeGrassInMap(Vector2d position) {
        Grass grass = Grass.createGrass(position, grassEnergy);

        map.place(grass);
        grasses.add(grass);
    }

    private List<Pair<Vector2d, Genome>> activateMagicEvolution() {
        Ensure.Is.MoreThen(magicEvolutionsLeft, 0, "magic evolutions left");
        if (animals.size() != 5)
            throw new IllegalStateException("There should be 5 animals on map, but " + animals.size() + " was found.");

        Platform.runLater(() ->
                new Alert(Alert.AlertType.INFORMATION, "Magic evolution activated.").showAndWait());

        magicEvolutionsLeft--;

        List<Pair<Vector2d, Genome>> animalsToPlace = new LinkedList<>();
        for (Animal animal : animals) {
            Vector2d magicAnimalPosition = map.getRandomUnoccupiedPosition();
            if (magicAnimalPosition != null)
                animalsToPlace.add(new Pair<>(magicAnimalPosition, animal.getGenome()));
            else
                break;
        }

        return animalsToPlace;
    }

    private void destroyDeadAnimals() {
        List<Pair<Vector2d, Genome>> magicAnimals = null;

        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (!animal.isAlive()) {
                animal.destroy();
                if (animalsDead > 0)
                    averageAnimalAliveTime *= (float) animalsDead / (animalsDead + 1);
                else
                    animalsDead++;
                averageAnimalAliveTime += (float) animal.getAge() / ++animalsDead;
                iterator.remove();
                WorldMapElementsStorage.store(animal);

                if (animals.size() == 5 && magicEvolutionsLeft > 0 && magicAnimals == null)
                    magicAnimals = activateMagicEvolution();
            }
        }

        if (magicAnimals != null)
            for (Pair<Vector2d, Genome> animalInfo : magicAnimals)
                placeAnimalInMap(animalInfo.getKey(), animalInitialEnergy, animalInfo.getValue());
    }

    private void moveAllAnimals() {
        animals.forEach(animal -> {
            animal.move();
            animal.makeOlder();
        });
    }

    private void eatGrasses() {
        List<? extends AbstractWorldMapElement> elements;

        if (animals.size() > grasses.size())
            elements = grasses;
        else
            elements = animals;

        Iterator<? extends AbstractWorldMapElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            AbstractWorldMapElement element = iterator.next();

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
                    grassOnTile.destroy();
                    if (element instanceof Grass)
                        iterator.remove();
                    else if (!grasses.remove(grassOnTile))
                        throw new IllegalStateException("Grass [" + grassOnTile + "] is not listed.");
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

        for (Animal animal : animalsToBePlaced)
            map.place(animal);
        animals.addAll(animalsToBePlaced);
    }

    private void announceEpochEnd() {
        for (IOnEpochEndInvokeObserver observer : epochEndObservers)
            observer.epochEnd();
        for (IOnEpochEndInvokeGUIObserver observer : epochEndGUIObservers)
            Platform.runLater(() -> observer.epochEnd(new EpochEndInfo(
                    animals.size(),
                    grasses.size(),
                    averageAnimalsEnergy,
                    averageAnimalAliveTime,
                    averageAnimalsOffspringCount,
                    dominantGenomes.stream().map(Genome::toString).toList()
            )));
    }

    private void calculateDominantGenomes() {
        dominantGenomes.clear();
        Map<Genome, Integer> genomesCount = new HashMap<>();
        int mostGenomesCount = 0;
        for (Animal animal : animals) {
            Genome animalGenome = animal.getGenome();
            genomesCount.putIfAbsent(animalGenome, 0);
            int currentGenomesCount = genomesCount.get(animalGenome) + 1;
            genomesCount.put(animalGenome, currentGenomesCount);
            if (currentGenomesCount > mostGenomesCount)
                mostGenomesCount = currentGenomesCount;
        }

        if (mostGenomesCount > 1)
            for (var entry : genomesCount.entrySet())
                if (entry.getValue() == mostGenomesCount)
                    dominantGenomes.add(entry.getKey());
    }

    private void calculateAverageAnimalsEnergy() {
        averageAnimalsEnergy = 0;
        for (Animal animal : animals)
            averageAnimalsEnergy += (float) animal.getEnergy() / animals.size();
    }

    private void calculateAverageAnimalsOffspringCount() {
        averageAnimalsOffspringCount = 0;
        for (Animal animal : animals)
            averageAnimalsOffspringCount += (float) animal.getOffspringCount() / animals.size();
    }

    @Override
    public void run() {
        try {
            calculateDominantGenomes();
            calculateAverageAnimalsEnergy();
            announceEpochEnd();
            while (isAlive) {
                while (!isStopped) {
                    Thread.sleep(timeDelay);
                    destroyDeadAnimals();
                    moveAllAnimals();
                    eatGrasses();
                    makeCrossovers();
                    calculateDominantGenomes();
                    calculateAverageAnimalsEnergy();
                    calculateAverageAnimalsOffspringCount();
                    tryAddGrassInMap(a -> b -> map.getRandomUnoccupiedPosition(a, b));
                    tryAddGrassInMap(a -> b -> map.getRandomUnoccupiedPositionReversed(a, b));
                    announceEpochEnd();
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR,
                            "Error occurred." + System.lineSeparator() + e.getMessage())
                            .showAndWait());
        } finally {
            epochEndObservers.clear();
            epochEndGUIObservers.clear();
            animals.forEach(Animal::destroy);
            animals.clear();
            grasses.forEach(Grass::destroy);
            grasses.clear();
        }
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
    public void addObserver(IOnEpochEndInvokeGUIObserver observer) {
        epochEndGUIObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnEpochEndInvokeGUIObserver observer) {
        epochEndGUIObservers.remove(observer);
    }

    public void stopSimulation() {
        isStopped = true;
    }

    public void resumeSimulation() {
        isStopped = false;
    }

    public void killSimulation() {
        isStopped = true;
        isAlive = false;
    }

    public List<Vector2d> getAllDominantGenomesPositions() {
        return animals.stream()
                .filter(animal -> dominantGenomes.contains(animal.getGenome()))
                .map(Animal::getPosition)
                .toList();
    }
}
