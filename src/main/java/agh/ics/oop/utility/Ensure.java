package agh.ics.oop.utility;

public class Ensure {
    private final boolean reversed;

    private Ensure(boolean reversed) {
        this.reversed = reversed;
    }

    public static final Ensure Is = new Ensure(false);
    public static final Ensure Not = new Ensure(true);

    private boolean isConditionTrue(boolean condition) {
        return (condition && reversed) || (!condition && !reversed);
    }

    public void Null(Object object, String argumentName) {
        if (isConditionTrue(object == null))
            throw new IllegalArgumentException(argumentName + (reversed ? " cannot" : " must") + " be null.");
    }

    public <T extends Comparable<T>>
    void InRange(T value, T lowerBound, T upperBound, String argumentName) {
        InRange(value, lowerBound, upperBound, argumentName, "");
    }

    public <T extends Comparable<T>>
    void InRange(T value, T lowerBound, T upperBound, String argumentName, String rangeName) {
        if (isConditionTrue(value.compareTo(lowerBound) >= 0 || value.compareTo(upperBound) <= 0))
            throw new IllegalArgumentException(
                    argumentName + " [ " + value + "] is " + (reversed ? "" : "not ") +
                            "within range " + rangeName + " (" + lowerBound + ", " + upperBound + "), but should " +
                            (reversed ? "not " : "") + "be.");
    }

    public <T extends Comparable<T>>
    void MoreThen(T value, T lowerBound, String argumentName) {
        MoreThen(value, lowerBound, argumentName, "");
    }

    public <T extends Comparable<T>>
    void MoreThen(T value, T lowerBound, String argumentName, String lowerBoundName) {
        if (isConditionTrue(value.compareTo(lowerBound) > 0))
            throw new IllegalArgumentException(
                    argumentName + " [" + value + "] is " + (reversed ? "" : "not ") +
                            "more then " + lowerBoundName + " [" + lowerBound + "], but should " +
                            (reversed ? "not " : "") + "be.");
    }

    public <T extends Comparable<T>>
    void LessThen(T value, T upperBound, String argumentName) {
        LessThen(value, upperBound, argumentName, "");
    }

    public <T extends Comparable<T>>
    void LessThen(T value, T upperBound, String argumentName, String upperBoundName) {
        if (isConditionTrue(value.compareTo(upperBound) < 0))
            throw new IllegalArgumentException(
                    argumentName + " [" + value + "] is " + (reversed ? "" : "not ") +
                            "less then " + upperBoundName + " [" + upperBound + "], but should " +
                            (reversed ? "not " : "") + "be.");
    }
}
