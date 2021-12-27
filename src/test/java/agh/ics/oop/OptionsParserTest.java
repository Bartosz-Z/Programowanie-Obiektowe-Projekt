package agh.ics.oop;

import agh.ics.oop.elements.MoveDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    @Test
    @DisplayName("Test correctness of OptionsParser when parsing String to MoveDirection")
    void testOptionsParserForAnimal() {
        OptionsParser parser = new OptionsParser();

        assertArrayEquals(new MoveDirection[] {
                        MoveDirection.FORWARD,
                        MoveDirection.BACKWARD,
                        MoveDirection.FORWARD,
                        MoveDirection.RIGHT,
                        MoveDirection.LEFT
                }, parser.parse(new String[]{
                        "f",
                        "b",
                        "forward",
                        "r",
                        "left"
                }),
                "Testing bunch of strings - 1");

        assertArrayEquals(new MoveDirection[] {
                        MoveDirection.BACKWARD,
                        MoveDirection.BACKWARD,
                        MoveDirection.BACKWARD,
                        MoveDirection.LEFT
                }, parser.parse(new String[]{
                        "backward",
                        "b",
                        "b",
                        "left"
                }),
                "Testing bunch of strings - 2");

        assertArrayEquals(new MoveDirection[] {
                        MoveDirection.RIGHT
                }, parser.parse(new String[]{
                        "right"
                }),
                "Testing bunch of strings - 3");

        assertThrows(IllegalArgumentException.class, () -> parser.parse(new String[]{
                        "asbr",
                        "left",
                        "r",
                        "",
                        "f",
                        "lll",
                        "backward"
                }),
                "Testing bunch of strings (with incorrect strings)");

        assertArrayEquals(new MoveDirection[] {}, parser.parse(null),
                "Testing null");
    }

}