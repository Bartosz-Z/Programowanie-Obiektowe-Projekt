package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    MoveDirection[] moveDirections = new OptionsParser().parse("f b r l f f r r f f".split(" "));
    Vector2d[] initialPositions = {
            new Vector2d(2,2),
            new Vector2d(3,4)
    };
    IWorldMap map = new RectangularMap(10, 5);

    SimulationEngine engine;

    @BeforeEach
    void setUp() { engine = new SimulationEngine(map, moveDirections, initialPositions); }

    @Test
    @DisplayName("Test if engine was constructed correctly")
    void testEngineConstruction() {
        Arrays.stream(initialPositions)
                .forEach(position -> assertInstanceOf(Animal.class, map.objectAt(position),
                        "There should be animal at position " + position.toString()));
    }

    @Test
    @DisplayName("Test engine simulation")
    void testRun() {
        engine.run();

        Object animal_1 = map.objectAt(new Vector2d(2, 2));
        assertInstanceOf(Animal.class, animal_1,
                "There should be an animal at position (2, 2) at this point");
        Object animal_2 = map.objectAt(new Vector2d(3, 4));
        assertInstanceOf(Animal.class, animal_2,
                "There should be an animal at position (3, 4) at this point");

        assertEquals(MapDirection.SOUTH, ((Animal)animal_1).getOrientation(),
                "Animal at position (2, 2) should face south at this point");
        assertEquals(MapDirection.NORTH, ((Animal)animal_2).getOrientation(),
                "Animal at position (3, 4) should face north at this point");
    }
}