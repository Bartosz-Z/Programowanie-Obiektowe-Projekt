package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapBoundaryTest {

    MapBoundary mapBoundary;

    @BeforeEach
    void setUp() {
        mapBoundary = new MapBoundary();
    }

    @Test
    @DisplayName("Test getUpperRight and getLowerLeft methods")
    void testGettingCorners() {
        assertNull(mapBoundary.getUpperRight(),
                "Upper right corner should be null in empty map boundary");
        assertNull(mapBoundary.getLowerLeft(),
                "Lower left corner should be null in empty map boundary");

        Vector2d vec = new Vector2d(3, 3);
        mapBoundary.addPosition(vec);

        assertEquals(vec, mapBoundary.getUpperRight(),
                "If only one vector2d is in map boundary it should be an upper right corner");
        assertEquals(vec, mapBoundary.getLowerLeft(),
                "If only one vector2d is in map boundary it should be a lower left corner");

        Vector2d vec2 = new Vector2d(4, 4);
        mapBoundary.addPosition(vec2);

        assertEquals(vec2, mapBoundary.getUpperRight(),
                "Test upper right if there are 2 2d vectors in map boundary");
        assertEquals(vec, mapBoundary.getLowerLeft(),
                "Test lower left if there are 2 2d vectors in map boundary");

        Vector2d vec3 = new Vector2d(2, 2);
        mapBoundary.addPosition(vec3);

        assertEquals(vec2, mapBoundary.getUpperRight(),
                "Test upper right if there are 3 2d vectors in map boundary");
        assertEquals(vec3, mapBoundary.getLowerLeft(),
                "Test lower left if there are 3 2d vectors in map boundary");
    }

    @Test
    @DisplayName("Test changing position")
    void testChangePosition() {
        Vector2d vec1 = new Vector2d(2, 2);
        Vector2d vec2 = new Vector2d(3, 3);
        Vector2d vec3 = new Vector2d(4, 4);

        mapBoundary.addPosition(vec1);
        mapBoundary.addPosition(vec2);
        mapBoundary.addPosition(vec2);
        mapBoundary.addPosition(vec3);

        Vector2d newVec1 = new Vector2d(5, 5);
        mapBoundary.positionChanged(null, vec3, newVec1);

        assertEquals(newVec1, mapBoundary.getUpperRight(),
                "Test changing upper right corner");

        Vector2d newVec2 = new Vector2d(1, 1);
        mapBoundary.positionChanged(null, vec2, newVec2);

        assertEquals(newVec2, mapBoundary.getLowerLeft(),
                "Test changing lower left corner");

        Vector2d newVec3 = new Vector2d(2, 2);
        mapBoundary.positionChanged(null, newVec1, newVec3);

        assertEquals(vec2, mapBoundary.getUpperRight(),
                "Test if 2 same vectors work in multiset");

        assertThrows(IllegalArgumentException.class, () -> mapBoundary.positionChanged(null, null, vec1),
                "positionChanged should throw IllegalArgumentException when 'oldPosition' is null");
        assertThrows(IllegalArgumentException.class, () -> mapBoundary.positionChanged(null, vec1, null),
                "positionChanged should throw IllegalArgumentException when 'newPosition' is null");
        assertThrows(IllegalArgumentException.class, () -> mapBoundary.positionChanged(null, new Vector2d(6, 6), vec1),
                "positionChanged should throw IllegalArgumentException when 'oldPosition' is not found in set");
    }
}