package agh.ics.oop;

public record TileUpdateInfo(Vector2d position, ImageName imageName) {
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof TileUpdateInfo tileUpdateInfo))
            return false;

        return position.equals(tileUpdateInfo.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
