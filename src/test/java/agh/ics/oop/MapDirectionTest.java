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
        assertEquals(mapDirection, MapDirection.EAST);
        mapDirection = mapDirection.next();
        assertEquals(mapDirection, MapDirection.SOUTH);
        mapDirection = mapDirection.next();
        assertEquals(mapDirection, MapDirection.WEST);
        mapDirection = mapDirection.next();
        assertEquals(mapDirection, MapDirection.NORTH);
    }

    // N -> W -> S -> E -> N
    @Test
    @DisplayName("Previous direction (counterclockwise)")
    void text_previous() {
        mapDirection = mapDirection.previous();
        assertEquals(mapDirection, MapDirection.WEST);
        mapDirection = mapDirection.previous();
        assertEquals(mapDirection, MapDirection.SOUTH);
        mapDirection = mapDirection.previous();
        assertEquals(mapDirection, MapDirection.EAST);
        mapDirection = mapDirection.previous();
        assertEquals(mapDirection, MapDirection.NORTH);
    }
}