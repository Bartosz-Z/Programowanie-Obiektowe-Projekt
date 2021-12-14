package agh.ics.oop;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class WorldMapElementsStorage {
    private final Map<Class<? extends AbstractWorldMapElement>, Queue<AbstractWorldMapElement>> storedElements;

    public WorldMapElementsStorage() {
        storedElements = new HashMap<>();
    }

    public <T extends AbstractWorldMapElement>
    AbstractWorldMapElement restore(Class<T> elementClass) {
        Queue<AbstractWorldMapElement> elements = storedElements.get(elementClass);
        if (elements == null)
            return null;
        return elements.poll();
    }

    public void store(AbstractWorldMapElement element) {
        storedElements.computeIfAbsent(element.getClass(), k -> new ArrayDeque<>()).add(element);
    }
}
