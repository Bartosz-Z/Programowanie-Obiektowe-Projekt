package agh.ics.oop.structures;

import java.util.List;

public record EpochEndInfo(
        int animalsCount,
        int grassCount,
        float averageAliveAnimalsEnergy,
        float averageAnimalAliveTime,
        float averageAnimalOffspringCount,
        List<String> dominantGenomes
) {}
