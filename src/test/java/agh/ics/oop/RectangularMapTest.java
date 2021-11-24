package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.jar.Manifest;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    RectangularMap map;

    @BeforeEach
    void setUp() { map = new RectangularMap(10, 5); }

    @Test
    @DisplayName("Test map boundaries in isAccessible method")
    void testIsAccessible() {
        assertTrue(map.isAccessible(new Vector2d(4, 2)),
                "Position within empty map boundaries should be accessible");
        assertFalse(map.isAccessible(new Vector2d(100, 42)),
                "Position outside map should not be accessible");
    }

    @Test
    @DisplayName("Test map boundaries in place method")
    void testPlace() {
        Animal animal = new Animal(map, new Vector2d(2, 2));

        assertTrue(map.place(animal),
                "Placing animal within empty map boundaries should return true");
        assertFalse(map.place(new Animal(map, new Vector2d(29, 12))),
                "Placing animal outside map should return false");
    }

    @Test
    @DisplayName("Test map boundaries in isOccupied method")
    void testIsOccupied() {
        assertFalse(map.isOccupied(new Vector2d(3, 19)),
                "Position outside map should not be occupied");
    }

    @Test
    @DisplayName("Test map boundaries in objectAt method")
    void testObjectAt() {
        assertNull(map.objectAt(new Vector2d(6, 12)),
                "There should be no objects outside map");
    }
}