package agh.ics.oop;

import java.util.LinkedList;
import java.util.List;

public class Animal extends AbstractWorldMapDynamicElement implements ILayerObservable {
    private int energy;
    private Genome genome;
    protected final List<ILayerChangeObserver> layerObservers;

    public Animal(AbstractWorldMap map, Vector2d initialPosition, int initialEnergy, Genome initialGenome) {
        super(map, initialPosition);

        if (initialEnergy <= 0)
            throw new IllegalArgumentException("'initialEnergy' argument should be positive");
        if (initialGenome == null)
            throw new IllegalArgumentException("'initialGenome' argument can not be null");

        energy = initialEnergy;
        genome = initialGenome;
        layerObservers = new LinkedList<>();
    }

    void updateState(Vector2d position, int energy, Genome genome) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null");
        if (energy <= 0)
            throw new IllegalArgumentException("'energy' argument should be positive");
        if (genome == null)
            throw new IllegalArgumentException("'genome' argument can not be null");

        this.position = position;
        this.energy = energy;
        this.genome = genome;
    }

    private void energyChanged(int newEnergy) {
        for (ILayerChangeObserver observer : layerObservers)
            observer.preLayerChanged(this);
        energy = newEnergy;
        for (ILayerChangeObserver observer : layerObservers)
            observer.postLayerChanged(this);
    }

    @Override
    public void move() {
        int activeGene = genome.getRandomGene();

        if (activeGene == 0) {
             if (tryChangePosition(position.add(mapDirection.toUnitVector())))
                 energyChanged(energy - 10);
        } else if (activeGene == 4) {
            if (tryChangePosition(position.subtract(mapDirection.toUnitVector())))
                energyChanged(energy - 10);
        } else if (activeGene < 4) {
            energyChanged(energy - 5);
            MapDirection newDirection = mapDirection;
            for (int i = 0; i < activeGene; i++)
                newDirection = newDirection.next();
            changeDirection(newDirection);
        } else {
            energyChanged(energy - 5);
            MapDirection newDirection = mapDirection;
            for (int i = 4; i < activeGene; i++)
                newDirection = newDirection.previous();
            changeDirection(newDirection);
        }
    }

    @Override
    public int getLayer() {
        return energy;
    }

    @Override
    public ImageName getImageName() {
        switch (mapDirection) {
            case NORTH -> { return ImageName.TILE_ANIMAL_NORTH; }
            case NORTH_EAST -> { return ImageName.TILE_ANIMAL_NORTH_EAST; }
            case EAST -> { return ImageName.TILE_ANIMAL_EAST; }
            case SOUTH_EAST -> { return ImageName.TILE_ANIMAL_SOUTH_EAST; }
            case SOUTH -> { return ImageName.TILE_ANIMAL_SOUTH; }
            case SOUTH_WEST -> { return ImageName.TILE_ANIMAL_SOUTH_WEST; }
            case WEST -> { return ImageName.TILE_ANIMAL_WEST; }
            case NORTH_WEST -> { return ImageName.TILE_ANIMAL_NORTH_WEST; }
        }
        throw new UnsupportedOperationException("'" + mapDirection + "' is not implemented.");
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
        return energy >= 0;
    }
}
