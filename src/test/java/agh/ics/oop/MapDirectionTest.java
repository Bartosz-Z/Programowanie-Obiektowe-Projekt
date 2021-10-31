package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    //   N
    // W   E
    //   S

    MapDirection mapDirection;

    @BeforeEach
    void setUp() {
        mapDirection = MapDirection.NORTH;
    }

    // N -> E -> S -> W -> N
    @Test
    @DisplayName("Next direction (clockwise)")
    void test_next() {
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.EAST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.SOUTH, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.WEST, mapDirection);
        mapDirection = mapDirection.next();
        assertEquals(MapDirection.NORTH, mapDirection);
    }

    // N -> W -> S -> E -> N
    @Test
    @DisplayName("Previous direction (counterclockwise)")
    void text_previous() {
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.WEST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.SOUTH, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.EAST, mapDirection);
        mapDirection = mapDirection.previous();
        assertEquals(MapDirection.NORTH, mapDirection);
    }
}