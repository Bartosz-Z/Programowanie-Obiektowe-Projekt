package agh.ics.oop;

public class Grass extends AbstractWorldMapElement {
    public Grass(Vector2d initialPosition) {
        super(initialPosition);
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
