package agh.ics.oop.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectsFactory {
    private final Map<Class<?>, Class<?>> wrapperToPrimitive;

    public ObjectsFactory() {
        wrapperToPrimitive = new HashMap<>();

        wrapperToPrimitive.put(Void.class, void.class);
        wrapperToPrimitive.put(Float.class, float.class);
        wrapperToPrimitive.put(Double.class, double.class);
        wrapperToPrimitive.put(Boolean.class, boolean.class);
        wrapperToPrimitive.put(Byte.class, byte.class);
        wrapperToPrimitive.put(Short.class, short.class);
        wrapperToPrimitive.put(Integer.class, int.class);
        wrapperToPrimitive.put(Long.class, long.class);
    }

    public <E> E createObject(Class<E> clazz, Object... args) {
        Class<?>[] classesOfArgs = Arrays.stream(args)
                .map(arg -> wrapperToPrimitive.getOrDefault(arg.getClass(), arg.getClass()))
                .toArray(Class[]::new);
        try {
            return clazz.getConstructor(classesOfArgs).newInstance(args);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't create object of that args: " + Arrays.toString(args));
        }
    }
}
