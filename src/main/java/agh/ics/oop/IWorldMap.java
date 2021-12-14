package agh.ics.oop;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IWorldMap {
    /**
     * Indicate if any object can access the given position.
     *
     * @param position
     *            The position checked for the position accessibility.
     * @return True if the object can access that position.
     */
    boolean isAccessible(Vector2d position);

    /**
     * Place an animal on the map.
     *
     * @param element
     *            The element to place on the map.
     */
    void place(AbstractWorldMapElement element);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the element.
     * @return element or null if the position is not occupied.
     */
    AbstractWorldMapElement objectAt(Vector2d position);
}