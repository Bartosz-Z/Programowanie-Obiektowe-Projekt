package agh.ics.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

    GrassField map;
    List<? extends AbstractWorldMapElement> grasses;

    @BeforeEach
    void setUp() {
        map = new GrassField(10);
        grasses = map.getWorldMapElements(Grass.class);
    }

    @Test
    @DisplayName("Test GrassFiled object construction")
    void testGrassFiled() {
        assertNotNull(grasses,
                "There should be some grasses in map, so list of them should not be null");
        assertEquals(10, grasses.size(),
                "There should be 10 grasses on the map");
    }

    @Test
    @DisplayName("Test grass position accessibility")
    void testIsAccessible() {
        Vector2d grassPosition = grasses.get(0).getPosition();

        assertTrue(map.isAccessible(new Vector2d(1000, 1000)),
                "Position at which is no grass should be accessible");
        assertTrue(map.isAccessible(grassPosition),
                "Position at which is grass should be accessible");
    }

    @Test
    @DisplayName("Test objectAt method")
    void testObjectAt() {
        AbstractWorldMapElement grass = grasses.get(0);

        assertEquals(grass, map.objectAt(grass.getPosition()),
                "objectAt method should return grass at position containing that grass");

        Animal animal = new Animal(map, grass.getPosition());
        map.place(animal);

        assertEquals(animal, map.objectAt(grass.getPosition()),
                "objectAt method should return animal over grass");
    }
}