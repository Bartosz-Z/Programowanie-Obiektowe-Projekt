package agh.ics.oop.maps;

import agh.ics.oop.gui.ImageName;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;

public abstract class AbstractJungleMap extends AbstractWorldMap {
    public final Vector2d jungleLowerLeftCorner, jungleUpperRightCorner;

    protected AbstractJungleMap(Vector2d size, float jungleRatio) {
        super(size);

        Ensure.Is.InRange(jungleRatio, 0f, 1f, "jungle ratio");

        jungleLowerLeftCorner = new Vector2d(
                Math.round(size.x() * (1 - jungleRatio) / 2),
                Math.round(size.y() * (1 - jungleRatio) / 2));
        jungleUpperRightCorner = new Vector2d(
                Math.round(size.x() * jungleRatio) - 1,
                Math.round(size.y() * jungleRatio) - 1).add(jungleLowerLeftCorner);
    }

    public boolean isInsideJungle(Vector2d position) {
        return position.follows(jungleLowerLeftCorner) && position.precedes(jungleUpperRightCorner);
    }

    @Override
    public ImageName getImageNameOfTile(Vector2d tilePosition) {
        if (tilePosition.follows(jungleLowerLeftCorner) && tilePosition.precedes(jungleUpperRightCorner))
            return ImageName.TILE_EMPTY_JUNGLE;
        return ImageName.TILE_EMPTY;
    }
}
