package agh.ics.oop.elements;

import agh.ics.oop.gui.ImageName;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.observers.ILayerChangeObserver;
import agh.ics.oop.observers.ILayerObservable;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;

import java.util.LinkedList;
import java.util.List;

public class Animal extends AbstractWorldMapDynamicElement implements ILayerObservable {
    private int currentEnergy;
    private final int maxEnergy;
    private Genome genome;
    protected final List<ILayerChangeObserver> layerObservers;

    private Animal(
            AbstractWorldMap map,
            Vector2d initialPosition,
            int initialEnergy,
            int maximumEnergy,
            Genome initialGenome) {
        super(map, initialPosition);

        Ensure.Is.MoreThen(initialEnergy, 0, "animal's initial energy");
        Ensure.Is.MoreThen(maximumEnergy, 0, "animal's maximum energy");
        Ensure.Not.Null(initialGenome, "animal's genome");

        currentEnergy = initialEnergy;
        maxEnergy = maximumEnergy;
        genome = initialGenome;
        layerObservers = new LinkedList<>();
    }

    public static Animal createAnimal(
            AbstractWorldMap map,
            Vector2d initialPosition,
            int initialEnergy,
            int maximumEnergy,
            Genome initialGenome) {

        Animal animal = WorldMapElementsStorage.restore(Animal.class);
        if (animal == null) {
            return new Animal(map, initialPosition, initialEnergy, maximumEnergy, initialGenome);
        } else {
            animal.updateState(map, initialPosition, initialEnergy, initialGenome);
            return animal;
        }
    }

    public Animal crossoverWith(Animal otherAnimal) {
        Ensure.Not.Null(otherAnimal, "other animal to crossover with");

        float parentsGenesRatio = (float)currentEnergy / (currentEnergy + otherAnimal.currentEnergy);
        int childEnergy = currentEnergy / 4;
        energyChanged(currentEnergy - childEnergy);
        childEnergy += otherAnimal.currentEnergy / 4;
        otherAnimal.energyChanged(otherAnimal.currentEnergy - otherAnimal.currentEnergy / 4);

        return createAnimal(
                map,
                position,
                childEnergy,
                maxEnergy,
                genome.crossoverWith(otherAnimal.getGenome(), parentsGenesRatio));
    }

    private void updateState(AbstractWorldMap map, Vector2d position, int energy, Genome genome) {
        Ensure.Not.Null(map, "animal's world map");
        Ensure.Not.Null(position, "animal's new position");
        Ensure.Is.MoreThen(energy, 0, "animal's new energy");
        Ensure.Not.Null(genome, "animal's new genome");

        this.map = map;
        this.position = position;
        this.currentEnergy = energy;
        this.genome = genome;
    }

    private void energyChanged(int newEnergy) {
        for (ILayerChangeObserver observer : layerObservers)
            observer.preLayerChanged(this);
        currentEnergy = newEnergy;
        for (ILayerChangeObserver observer : layerObservers)
            observer.postLayerChanged(this);
    }

    @Override
    public void move() {
        int activeGene = genome.getRandomGene();

        if (activeGene == 0) {
             if (tryChangePosition(position.add(mapDirection.toUnitVector())))
                 energyChanged(currentEnergy - 10);
             else
                 energyChanged(currentEnergy - 4);
        } else if (activeGene == 4) {
            if (tryChangePosition(position.subtract(mapDirection.toUnitVector())))
                energyChanged(currentEnergy - 11);
            else
                energyChanged(currentEnergy - 4);
        } else if (activeGene < 4) {
            energyChanged(currentEnergy - 5);
            MapDirection newDirection = mapDirection;
            for (int i = 0; i < activeGene; i++)
                newDirection = newDirection.next();
            changeDirection(newDirection);
        } else {
            energyChanged(currentEnergy - 5);
            MapDirection newDirection = mapDirection;
            for (int i = 4; i < activeGene; i++)
                newDirection = newDirection.previous();
            changeDirection(newDirection);
        }
    }

    @Override
    public int getLayer() {
        return currentEnergy;
    }

    @Override
    public ImageName getImageName() {
        return switch (mapDirection) {
            case NORTH -> ImageName.TILE_ANIMAL_NORTH;
            case NORTH_EAST -> ImageName.TILE_ANIMAL_NORTH_EAST;
            case EAST -> ImageName.TILE_ANIMAL_EAST;
            case SOUTH_EAST -> ImageName.TILE_ANIMAL_SOUTH_EAST;
            case SOUTH -> ImageName.TILE_ANIMAL_SOUTH;
            case SOUTH_WEST -> ImageName.TILE_ANIMAL_SOUTH_WEST;
            case WEST -> ImageName.TILE_ANIMAL_WEST;
            case NORTH_WEST -> ImageName.TILE_ANIMAL_NORTH_WEST;
        };
    }

    @Override
    public void destroy() {
        super.destroy();
        layerObservers.clear();
    }

    @Override
    public void addObserver(ILayerChangeObserver observer) {
        layerObservers.add(observer);
    }

    @Override
    public void removeObserver(ILayerChangeObserver observer) {
        layerObservers.remove(observer);
    }

    public boolean isAlive() {
        return currentEnergy > 0;
    }

    public void addEnergy(int energy) {
        energyChanged(Math.min(currentEnergy + energy, maxEnergy));
    }

    public int getEnergy() {
        return currentEnergy;
    }

    public Genome getGenome() {
        return genome;
    }

    public float getEnergyPercentage() {
        return ((float) currentEnergy) / maxEnergy;
    }
}