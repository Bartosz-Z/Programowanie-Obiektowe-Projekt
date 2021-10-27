package agh.ics.oop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    @DisplayName("Test equals method with various objects")
    void test_equals() {
        Vector2d vec = new Vector2d(0, 0);
        assertEquals(vec, vec,
                "Test vector2d equality with itself");
        assertEquals(vec, new Vector2d(0, 0),
                "Test vector2d equality with it's copy");
        assertNotEquals(vec, new Vector2d(1, 1),
                "Test vector2d equality with different vector");
        assertNotEquals(vec, new Object(),
                "Test vector2d equality with other object type");
        assertNotEquals(vec, null,
                "Test vector2d equality with null");
    }

    @Test
    @DisplayName("Test toString method")
    void test_toString() {
        assertEquals(new Vector2d(1, 1).toString(), "(1,1)",
                "Test toString for both x,y positive");
        assertEquals(new Vector2d(-1, -1).toString(), "(-1,-1)",
                "Test toString for x both negative");
        assertEquals(new Vector2d(0, 0).toString(), "(0,0)",
                "Test toString for both x,y zero");
    }

    @Test
    @DisplayName("Test precedes method with various 2d vectors")
    void test_precedes() {
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
    void follows() {
        Vector2d vector = new Vector2d(3, 3);
        assertTrue(vector.follows(vector),
                "Vector2d should follows itself");
        assertTrue(vector.follows(new Vector2d(2, 2)),
                "Vector2d should follows other vector2d with both x,y smaller");
        assertTrue(vector.follows(new Vector2d(3, 2)),
                "Vector2d should follows other vector2d with x equal, y smaller");
        assertTrue(vector.follows(new Vector2d(2, 3)),
                "Vector2d should follows other vector2d with x smaller, y equal");
        assertFalse(vector.follows(new Vector2d(4, 4)),
                "Vector2d should not follows other vector2d with both x,y greater");
        assertThrows(IllegalArgumentException.class, () -> vector.follows(null),
                "Follows method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test lowerLeft method")
    void test_lowerLeft() {
        Vector2d vector = new Vector2d(10, 20);
        assertEquals(vector.lowerLeft(vector), vector,
                "Lower left point of 2 same vectors2d should be the same Vector2d");
        assertEquals(vector.lowerLeft(new Vector2d(30, 35)), vector,
                "Test when one of the vectors is the result");
        assertEquals(new Vector2d(30, 35).lowerLeft(vector), vector,
                "Test commutative of lowerLeft method");
        assertEquals(vector.lowerLeft(new Vector2d(15, 5)), new Vector2d(10, 5),
                "Test when none of the vectors2d is the result");
        assertThrows(IllegalArgumentException.class, () -> vector.lowerLeft(null),
                "LowerLeft method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test upperRight method")
    void test_upperRight() {
        Vector2d vector = new Vector2d(20, 25);
        assertEquals(vector.upperRight(vector), vector,
                "Upper right point of 2 same vectors2d should be the same Vector2d");
        assertEquals(vector.upperRight(new Vector2d(15, 10)), vector,
                "Test when one of the vectors is the result");
        assertEquals(new Vector2d(15, 10).upperRight(vector), vector,
                "Test commutative of lowerLeft method");
        assertEquals(vector.upperRight(new Vector2d(15, 30)), new Vector2d(20, 30),
                "Test when none of the vectors2d is the result");
        assertThrows(IllegalArgumentException.class, () -> vector.upperRight(null),
                "UpperRight method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test Vector2d addition")
    void test_add() {
        Vector2d vector = new Vector2d(4, 12);
        assertEquals(vector.add(new Vector2d(-3, 2)), new Vector2d(1, 14),
                "Result Vector2d should be (x1 + x2, y1 + y2)");
        assertThrows(IllegalArgumentException.class, () -> vector.add(null),
                "Add method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test Vector2d subtraction")
    void test_subtract() {
        Vector2d vector = new Vector2d(-9, 62);
        assertEquals(vector.subtract(new Vector2d(41, 16)), new Vector2d(-50, 46),
                "Result Vector2d should be (x1 - x2, y1 - y2)");
        assertThrows(IllegalArgumentException.class, () -> vector.subtract(null),
                "Subtract method should throw IllegalArgumentException when argument is null");
    }

    @Test
    @DisplayName("Test opposite method")
    void test_opposite() {
        Vector2d vector = new Vector2d(-2, 3);
        assertEquals(vector.opposite(), new Vector2d(2, -3),
                "Test simple opposition");
        assertEquals(vector.opposite().opposite(), vector,
                "Double opposition should negate each other");
    }
}