package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {

    AbstractWorldMap map;

    @BeforeEach
    void setUp() {
        map = new AbstractWorldMap() {
            @Override
            public boolean isAccessible(Vector2d position) {
                if (position == null)
                    throw new IllegalArgumentException("'position' argument can not be null.");
                return !isOccupied(position);
            }
        };
    }

    @Test
    @DisplayName("Test accessibility of position")
    void testIsAccessible() {
        assertTrue(map.isAccessible(new Vector2d(4, 2)),
                "Position in empty map should be accessible");

        Animal animal = new Animal(map, new Vector2d(2, 2));
        map.place(animal);

        assertFalse(map.isAccessible(animal.getPosition()),
                "Position at which is an animal should not be accessible");
        assertTrue(map.isAccessible(new Vector2d(3, 3)),
                "Position in non-empty map, but not containing animal should be accessible");
        assertThrows(IllegalArgumentException.class, () -> map.isAccessible(null),
                "isAccessible method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test placing animals on the map")
    void testPlace() {
        Animal animal = new Animal(map, new Vector2d(2, 2));

        assertDoesNotThrow(() -> map.place(animal),
                "Placing animal in empty map should not throw exception");
        assertThrows(IllegalArgumentException.class, () -> map.place(new Animal(map, animal.getPosition())),
                "Placing animal in the same place with another animal should throw exception");

        assertThrows(IllegalArgumentException.class, () -> map.place(null),
                "Place method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test position occupation")
    void testIsOccupied() {
        assertFalse(map.isOccupied(new Vector2d(4, 2)),
                "Position in empty map should not be occupied");

        Animal animal = new Animal(map, new Vector2d(2, 2));
        map.place(animal);

        assertTrue(map.isOccupied(animal.getPosition()),
                "Position containing animal should be occupied");
        assertFalse(map.isOccupied(new Vector2d(1, 1)),
                "Position in non-empty map, but not containing animal should not be occupied");
        assertThrows(IllegalArgumentException.class, () -> map.isOccupied(null),
                "isOccupied method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test ObjectAt method")
    void testObjectAt() {
        assertNull(map.objectAt(new Vector2d(4, 2)),
                "There should be no objects in empty map");

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