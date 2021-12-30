package agh.ics.oop.gui;

public enum ImageName {
    TILE_BLANK("BlankTile.png"),
    TILE_EMPTY("EmptyTile.png"),
    TILE_EMPTY_JUNGLE("EmptyTileJungle.png"),
    TILE_GRASS("Grass.png"),
    TILE_ANIMAL_NORTH("AnimalNorth.png"),
    TILE_ANIMAL_NORTH_EAST("AnimalNorthEast.png"),
    TILE_ANIMAL_EAST("AnimalEast.png"),
    TILE_ANIMAL_SOUTH_EAST("AnimalSouthEast.png"),
    TILE_ANIMAL_SOUTH("AnimalSouth.png"),
    TILE_ANIMAL_SOUTH_WEST("AnimalSouthWest.png"),
    TILE_ANIMAL_WEST("AnimalWest.png"),
    TILE_ANIMAL_NORTH_WEST("AnimalNorthWest.png"),
    TILE_HIGHLIGHTATION("Highlightation.png");

    public final String name;

    ImageName(String name) {
        this.name = name;
    }
}
