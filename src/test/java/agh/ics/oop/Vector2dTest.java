package agh.ics.oop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    @DisplayName("Test equals method with various objects")
    void testEquals() {
        Vector2d vec = Vector2d.zero;
        assertEquals(vec, vec,
                "Test vector2d equality with itself");
        assertEquals(Vector2d.zero, vec,
                "Test vector2d equality with it's copy");
        assertNotEquals(new Vector2d(1, 1), vec,
                "Test vector2d equality with different vector");
        assertNotEquals(new Object(), vec,
                "Test vector2d equality with other object type");
        assertNotEquals(null, vec,
                "Test vector2d equality with null");
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        assertEquals("(1,1)", new Vector2d(1, 1).toString(),
                "Test toString for both x,y positive");
        assertEquals("(-1,-1)", new Vector2d(-1, -1).toString(),
                "Test toString for x both negative");
        assertEquals("(0,0)", Vector2d.zero.toString(),
                "Test toString for both x,y zero");
    }

    @Test
    @DisplayName("Test precedes method with various 2d vectors")
    void testPrecedes() {
        Vector2d vector = new Vector2d(3, 3);
        assertTrue(vector.precedes(vector),
                "Vector2d should precedes itself");
        assertTrue(vector.precedes(new Vector2d(4, 4)),
                "Vector2d should precedes other vector2d with both x,y greater");
        assertTrue(vector.precedes(new Vector2d(3, 4)),
                "Vector2d should precedes other vector2d with x equal, y greater");
        assertTrue(vector.precedes(new Vector2d(4, 3)),
                "Vector2d should precedes other vector2d with x greater, y equal");
        assertFalse(vector.precedes(new Vector2d(2, 2)),
                "Vector2d should not precedes other vector2d with both x,y smaller");
        assertThrows(IllegalArgumentException.class, () -> vector.precedes(null),
                "Precedes method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test follow method with various 2d vectors")
    void testFollows() {
        Vector2d vector = new Vector2d(3, 3);
        assertTrue(vector.follows(vector),
                "Vector2d should follow itself");
        assertTrue(vector.follows(new Vector2d(2, 2)),
                "Vector2d should follow other vector2d with both x,y smaller");
        assertTrue(vector.follows(new Vector2d(3, 2)),
                "Vector2d should follow other vector2d with x equal, y smaller");
        assertTrue(vector.follows(new Vector2d(2, 3)),
                "Vector2d should follow other vector2d with x smaller, y equal");
        assertFalse(vector.follows(new Vector2d(4, 4)),
                "Vector2d should not follow other vector2d with both x,y greater");
        assertThrows(IllegalArgumentException.class, () -> vector.follows(null),
                "Follows method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test lowerLeft method")
    void testLowerLeft() {
        Vector2d vector = new Vector2d(10, 20);
        assertEquals(vector, vector.lowerLeft(vector),
                "Lower left point of 2 same vectors2d should be the same Vector2d");
        assertEquals(vector, vector.lowerLeft(new Vector2d(30, 35)),
                "Test when one of the vectors is the result");
        assertEquals(vector, new Vector2d(30, 35).lowerLeft(vector),
                "Test commutative of lowerLeft method");
        assertEquals(new Vector2d(10, 5), vector.lowerLeft(new Vector2d(15, 5)),
                "Test when none of the vectors2d is the result");
        assertThrows(IllegalArgumentException.class, () -> vector.lowerLeft(null),
                "LowerLeft method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test upperRight method")
    void testUpperRight() {
        Vector2d vector = new Vector2d(20, 25);
        assertEquals(vector, vector.upperRight(vector),
                "Upper right point of 2 same vectors2d should be the same Vector2d");
        assertEquals(vector, vector.upperRight(new Vector2d(15, 10)),
                "Test when one of the vectors is the result");
        assertEquals(vector, new Vector2d(15, 10).upperRight(vector),
                "Test commutative of lowerLeft method");
        assertEquals(new Vector2d(20, 30), vector.upperRight(new Vector2d(15, 30)),
                "Test when none of the vectors2d is the result");
        assertThrows(IllegalArgumentException.class, () -> vector.upperRight(null),
                "UpperRight method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test Vector2d addition")
    void testAdd() {
        Vector2d vector = new Vector2d(4, 12);
        assertEquals(new Vector2d(1, 14), vector.add(new Vector2d(-3, 2)),
                "Result Vector2d should be (x1 + x2, y1 + y2)");
        assertThrows(IllegalArgumentException.class, () -> vector.add(null),
                "Add method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test Vector2d subtraction")
    void testSubtract() {
        Vector2d vector = new Vector2d(-9, 62);
        assertEquals(new Vector2d(-50, 46), vector.subtract(new Vector2d(41, 16)),
                "Result Vector2d should be (x1 - x2, y1 - y2)");
        assertThrows(IllegalArgumentException.class, () -> vector.subtract(null),
                "Subtract method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test opposite method")
    void testOpposite() {
        Vector2d vector = new Vector2d(-2, 3);
        assertEquals(new Vector2d(2, -3), vector.opposite(),
                "Test simple opposition");
        assertEquals(vector, vector.opposite().opposite(),
                "Double opposition should negate each other");
    }
}