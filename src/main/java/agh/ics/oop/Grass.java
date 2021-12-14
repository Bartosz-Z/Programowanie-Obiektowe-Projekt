package agh.ics.oop;

public class Grass extends AbstractWorldMapElement {
    public final int energy;

    public Grass(Vector2d initialPosition, int energy) {
        super(initialPosition);
        this.energy = energy;
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
