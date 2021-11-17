package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.jar.Manifest;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    RectangularMap map;

    @BeforeEach
    void setUp() { map = new RectangularMap(10, 5); }

    @Test
    void testIsAccessible() {
        assertTrue(map.isAccessible(new Vector2d(4, 2)),
                "Position within empty map boundaries should be accessible");
        assertFalse(map.isAccessible(new Vector2d(100, 42)),
                "Position outside map should not be accessible");

        Animal animal = new Animal(map, new Vector2d(2, 2));
        map.place(animal);

        assertFalse(map.isAccessible(animal.getPosition()),
                "Position at which is an animal should not be accessible");
        assertTrue(map.isAccessible(new Vector2d(3, 3)),
                "Position within non-empty map boundaries, but not containing animal should be accessible");
        assertThrows(IllegalArgumentException.class, () -> map.isAccessible(null),
                "isAccessible method should throw IllegalArgumentException when argument is null");
    }

    @Test
    void testPlace() {
        Animal animal = new Animal(map, new Vector2d(2, 2));
        assertTrue(map.place(animal),
                "Placing animal within empty map boundaries should return true");
        assertFalse(map.place(new Animal(map, animal.getPosition())),
                "Placing animal in the same place with another animal should return false");
        assertFalse(map.place(new Animal(map, new Vector2d(29, 12))),
                "Placing animal outside map should return false");

        assertThrows(IllegalArgumentException.class, () -> map.place(null),
                "Place method should throw IllegalArgumentException when argument is null");
    }

    @Test
    void testIsOccupied() {
        assertFalse(map.isOccupied(new Vector2d(4, 2)),
                "Position within empty map boundaries should not be occupied");
        assertFalse(map.isOccupied(new Vector2d(3, 19)),
                "Position outside map should not be occupied");

        Animal animal = new Animal(map, new Vector2d(2, 2));
        map.place(animal);

        assertTrue(map.isOccupied(animal.getPosition()),
                "Position containing animal should be occupied");
        assertFalse(map.isOccupied(new Vector2d(1, 1)),
                "Position within non-empty map boundaries, but not containing animal should not be occupied");
        assertThrows(IllegalArgumentException.class, () -> map.isOccupied(null),
                "isOccupied method should throw IllegalArgumentException when argument is null");
    }

    @Test
    void testObjectAt() {
        assertNull(map.objectAt(new Vector2d(4, 2)),
                "There should be no objects within empty map boundaries");
        assertNull(map.objectAt(new Vector2d(6, 12)),
                "There should be no objects outside map");

        Animal animal = new Animal(map, new Vector2d(2, 2));
        map.place(animal);

        assertEquals(animal, map.objectAt(animal.getPosition()),
                "objectAt method should return animal at position containing that animal");
        assertNull(map.objectAt(new Vector2d(1, 1)),
                "There should be no object in non-empty map at position not containing anything");
        assertThrows(IllegalArgumentException.class, () -> map.objectAt(null),
                "objectAt method should throw IllegalArgumentException when argument is null");
    }
}