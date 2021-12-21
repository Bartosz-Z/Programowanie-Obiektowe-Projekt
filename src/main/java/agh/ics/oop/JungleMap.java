package agh.ics.oop;

public class JungleMap extends AbstractJungleMap {

    public JungleMap(Vector2d size, float jungleRatio) {
        super(size, jungleRatio);
    }

    public JungleMap(int width, int height, float jungleRatio) {
        this(new Vector2d(width, height), jungleRatio);
    }

    @Override
    public boolean isAccessible(Vector2d position) {
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");

        return position.follows(Vector2d.zero) && position.precedes(size.subtract(new Vector2d(1, 1)));
    }
}
