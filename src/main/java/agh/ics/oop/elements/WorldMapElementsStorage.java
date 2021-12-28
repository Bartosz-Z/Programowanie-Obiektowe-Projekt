package agh.ics.oop.elements;

import agh.ics.oop.utility.Ensure;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class WorldMapElementsStorage {
    private static final Map<Class<? extends AbstractWorldMapElement>, Queue<AbstractWorldMapElement>> storedElements =
            new HashMap<>();

    private WorldMapElementsStorage() {}

    @SuppressWarnings("unchecked")
    public static synchronized <E extends AbstractWorldMapElement>
    E restore(Class<E> elementClass) {
        Queue<AbstractWorldMapElement> elements = storedElements.get(elementClass);
        if (elements == null)
            return null;

        AbstractWorldMapElement element = elements.poll();
        if (element == null)
            return null;

        if (elementClass.isInstance(element))
            return (E)element;
        else
            throw new IllegalStateException("Storage is in illegal state deu to [" + element + "] element.");
    }

    public static synchronized void store(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element to store");

        storedElements.computeIfAbsent(element.getClass(), k -> new ArrayDeque<>()).add(element);
    }
}
