package agh.ics.oop.maps;

import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;

public class JungleMap extends AbstractJungleMap {

    public JungleMap(Vector2d size, float jungleRatio) {
        super(size, jungleRatio);
    }

    public JungleMap(int width, int height, float jungleRatio) {
        this(new Vector2d(width, height), jungleRatio);
    }

    @Override
    public boolean isAccessible(Vector2d position) {
        Ensure.Not.Null(position, "position");

        return position.follows(Vector2d.zero) && position.precedes(new Vector2d(size.x() - 1, size.y() - 1));
    }
}
