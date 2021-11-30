package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


class AnimalTest {
    //   N
    // W   E
    //   S

    Animal animal;
    AbstractWorldMap map;

    @BeforeEach
    void setUp() {
        map = new RectangularMap(4, 4);
        animal = new Animal(map);
        map.place(animal);
    }

    @Test
    @DisplayName("Test isAt method")
    void testIsAt() {
        assertTrue(animal.isAt(animal.getPosition()),
                "isAt method should return True if animal is indeed in given position.");
        assertFalse(animal.isAt(animal.getPosition().add(new Vector2d(1, 1))),
                "isAt method should return False if animal is not in given position.");

        assertThrows(IllegalArgumentException.class, () -> animal.isAt(null),
                "isAt method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test Animal orientation")
    void testAnimalOrientation() {
        assertEquals(MapDirection.NORTH, animal.getOrientation(),
                "Animal should face north when created");

        // Test right rotation
        animal.move(MoveDirection.RIGHT);
        assertEquals(MapDirection.EAST, animal.getOrientation(),
                "Animal should face east after turning right from north");
        animal.move(MoveDirection.RIGHT);
        assertEquals(MapDirection.SOUTH, animal.getOrientation(),
                "Animal should face south after turning right from east");
        animal.move(MoveDirection.RIGHT);
        assertEquals(MapDirection.WEST, animal.getOrientation(),
                "Animal should face west after turning right from south");
        animal.move(MoveDirection.RIGHT);
        assertEquals(MapDirection.NORTH, animal.getOrientation(),
                "Animal should face north after turning right from west");

        // Test left rotation
        animal.move(MoveDirection.LEFT);
        assertEquals(MapDirection.WEST, animal.getOrientation(),
                "Animal should face west after turning left from north");
        animal.move(MoveDirection.LEFT);
        assertEquals(MapDirection.SOUTH, animal.getOrientation(),
                "Animal should face south after turning left from west");
        animal.move(MoveDirection.LEFT);
        assertEquals(MapDirection.EAST, animal.getOrientation(),
                "Animal should face east after turning left from south");
        animal.move(MoveDirection.LEFT);
        assertEquals(MapDirection.NORTH, animal.getOrientation(),
                "Animal should face north after turning left from east");

        assertThrows(IllegalArgumentException.class, () -> animal.move(null),
                "Move method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test Animal movement")
    void testAnimalMovement() {
        assertTrue(animal.isAt(new Vector2d(2, 2)),
                "Animal should be at (2,2) position when created");

        animal.move(MoveDirection.FORWARD);
        assertTrue(animal.isAt(new Vector2d(2, 3)),
                "Animal should be at (2,3) after moving forward when facing north from (2,2)");
        animal.move(MoveDirection.BACKWARD);
        assertTrue(animal.isAt(new Vector2d(2, 2)),
                "Animal should be at (2,2) after moving backward when facing north from (2,3)");

        animal.move(MoveDirection.RIGHT);
        animal.move(MoveDirection.FORWARD);
        assertTrue(animal.isAt(new Vector2d(3, 2)),
                "Animal should be at (3,2) after moving forward when facing east from (2,2)");
        animal.move(MoveDirection.BACKWARD);
        assertTrue(animal.isAt(new Vector2d(2, 2)),
                "Animal should be at (2,2) after moving backward when facing east from (3,2)");

        animal.move(MoveDirection.RIGHT);
        animal.move(MoveDirection.FORWARD);
        assertTrue(animal.isAt(new Vector2d(2, 1)),
                "Animal should be at (2,1) after moving forward when facing south from (2,2)");
        animal.move(MoveDirection.BACKWARD);
        assertTrue(animal.isAt(new Vector2d(2, 2)),
                "Animal should be at (2,2) after moving backward when facing south from (2,1)");

        animal.move(MoveDirection.RIGHT);
        animal.move(MoveDirection.FORWARD);
        assertTrue(animal.isAt(new Vector2d(1, 2)),
                "Animal should be at (1,2) after moving forward when facing west from (2,2)");
        animal.move(MoveDirection.BACKWARD);
        assertTrue(animal.isAt(new Vector2d(2, 2)),
                "Animal should be at (2,2) after moving backward when facing west from (2,1)");

        assertThrows(IllegalArgumentException.class, () -> animal.move(null),
                "Move method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Check if Animal is always within map")
    void testAnimalOutOfBoundary() {
        Function<Integer, Void> moveAnimalForward = numberOfSteps -> {
                for (int i = 0; i < numberOfSteps; i++) animal.move(MoveDirection.FORWARD);
                return null; };

        assertTrue(animal.isAt(animal.getPosition()),
                "Animal should be on the map when created");

        //Checking north boundary
        // Moving animal 3 steps forward so: (2,2) -> (2,5)
        moveAnimalForward.apply(3);
        assertTrue(animal.isAt(animal.getPosition()),
                "Animal should be on the map (north boundary)");
        assertTrue(animal.isAt(new Vector2d(2, 4)),
                "Animal should be at (2,4) at this point (north boundary)");

        //Checking east boundary
        //Rotating animal, so it faces east
        animal.move(MoveDirection.RIGHT);
        // Moving animal 3 steps forward so: (2,4) -> (5,4)
        moveAnimalForward.apply(3);
        assertTrue(animal.isAt(animal.getPosition()),
                "Animal should be on the map (east boundary)");
        assertTrue(animal.isAt(new Vector2d(4, 4)),
                "Animal should be at (4,4) at this point (east boundary)");

        //Checking south boundary
        //Rotating animal, so it faces south
        animal.move(MoveDirection.RIGHT);
        // Moving animal 5 steps forward so: (4,4) -> (4,-1)
        moveAnimalForward.apply(5);
        assertTrue(animal.isAt(animal.getPosition()),
                "Animal should be on the map (south boundary)");
        assertTrue(animal.isAt(new Vector2d(4, 0)),
                "Animal should be at (4,0) at this point (south boundary)");

        //Checking west boundary
        //Rotating animal, so it faces west
        animal.move(MoveDirection.RIGHT);
        // Moving animal 5 steps forward so: (4,0) -> (-1,0)
        moveAnimalForward.apply(5);
        assertTrue(animal.isAt(animal.getPosition()),
                "Animal should be on the map (west boundary)");
        assertTrue(animal.isAt(new Vector2d(0, 0)),
                "Animal should be at (0,0) at this point (west boundary)");
    }
}