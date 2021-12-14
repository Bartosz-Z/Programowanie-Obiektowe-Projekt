package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    // NW N NE
    //  W   E
    // SW S SE

    MapDirection mapDirection;

    @BeforeEach
    void setUp() {
        mapDirection = MapDirection.NORTH;
    }

    // N -> NE -> E -> SE -> S -> SW -> W -> NW -> N
    @Test
    @DisplayName("Next direction (clockwise)")
    void test_next() {
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.NORTH_EAST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.EAST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.SOUTH_EAST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.SOUTH, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.SOUTH_WEST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.WEST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.NORTH_WEST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.NORTH, mapDirection);
    }

    // N -> NW -> W -> SW -> S -> SE -> E -> NE -> N
    @Test
    @DisplayName("Previous direction (counterclockwise)")
    void text_previous() {
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.NORTH_WEST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.WEST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.SOUTH_WEST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.SOUTH, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.SOUTH_EAST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.EAST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.NORTH_EAST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.NORTH, mapDirection);
    }
}