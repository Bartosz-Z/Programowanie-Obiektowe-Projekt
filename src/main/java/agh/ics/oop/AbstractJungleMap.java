package agh.ics.oop;

public abstract class AbstractJungleMap extends AbstractWorldMap {
    private final Vector2d jungleLowerLeftCorner, jungleUpperRightCorner;

    protected AbstractJungleMap(Vector2d size, float jungleRatio) {
        super(size);

        if (jungleRatio < 0 || jungleRatio > 1)
            throw new IllegalArgumentException("'jungleRatio' argument should be between 0 and 1");

        jungleLowerLeftCorner = new Vector2d(
                Math.round(size.x() * (1 - jungleRatio) / 2),
                Math.round(size.y() * (1 - jungleRatio) / 2));
        jungleUpperRightCorner = new Vector2d(
                Math.round(size.x() * jungleRatio) - 1,
                Math.round(size.y() * jungleRatio) - 1).add(jungleLowerLeftCorner);
    }

    @Override
    public ImageName getImageNameOfTile(Vector2d tilePosition) {
        if (tilePosition.follows(jungleLowerLeftCorner) && tilePosition.precedes(jungleUpperRightCorner))
            return ImageName.TILE_EMPTY_JUNGLE;
        return ImageName.TILE_EMPTY;
    }
}
