package agh.ics.oop.elements;

import agh.ics.oop.gui.ImageName;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;

public class Grass extends AbstractWorldMapElement {
    public final int energy;

    private Grass(Vector2d initialPosition, int energy) {
        super(initialPosition);

        Ensure.Is.MoreThen(energy, 0, "grass's energy");
        this.energy = energy;
    }

    public static Grass createGrass(Vector2d initialPosition, int energy) {
        Grass grass = WorldMapElementsStorage.restore(Grass.class);
        if (grass == null) {
            return new Grass(initialPosition, energy);
        } else {
            grass.updateState(initialPosition);
            return grass;
        }
    }

    private void updateState(Vector2d position) {
        Ensure.Not.Null(position, "grass's new position");

        this.position = position;
    }

    @Override
    public int getLayer() {
        return -1;
    }

    @Override
    public ImageName getImageName() {
        return ImageName.TILE_GRASS;
    }
}
