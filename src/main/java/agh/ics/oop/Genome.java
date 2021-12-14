package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Genome {
    private final int[] genes;

    public Genome(int[] genes) {
        if (genes == null)
            throw new IllegalArgumentException("'genes' argument can not be null");
        if (genes.length != 32)
            throw new IllegalArgumentException("'genes' argument length should be 32");

        this.genes = genes.clone();
    }

    public int getRandomGene() {
        return genes[ThreadLocalRandom.current().nextInt(genes.length)];
    }

    public Genome crossoverWith(Genome otherGenome, float genesRatio) {
        if (otherGenome == null)
            throw new IllegalArgumentException("'other' argument can not be null");
        if (genesRatio < 0 || genesRatio > 1)
            throw new IllegalArgumentException("'genesRatio' argument should be between 0 and 1");

        int otherGenomeGenesInherited = Math.round(genes.length * genesRatio);
        int[] genesCrossover = new int[32];
        int[] genesIndexes = new int[32];

        for (int i = 0; i < genesIndexes.length; i++)
            genesIndexes[i] = i;

        // Array shuffle from stackoverflow
        Random random = ThreadLocalRandom.current();
        for (int i = genesIndexes.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = genesIndexes[index];
            genesIndexes[index] = genesIndexes[i];
            genesIndexes[i] = temp;
        }
        // ---

        for (int i = 0; i < otherGenomeGenesInherited; i++)
            genesCrossover[genesIndexes[i]] = otherGenome.getGene(genesIndexes[i]);
        for (int i = otherGenomeGenesInherited; i < genesIndexes.length; i++)
            genesCrossover[genesIndexes[i]] = genes[genesIndexes[i]];

        return new Genome(genesCrossover);
    }

    public int getGene(int geneIndex) {
        return genes[geneIndex];
    }

    public Genome getGenomeClone() {
        return new Genome(genes);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Genome otherGenome))
            return false;

        int[] thisGenesTypeCount = new int[8];
        int[] otherGenesTypeCount = new int[8];

        for (int i = 0; i < genes.length; i++) {
            thisGenesTypeCount[genes[i]]++;
            otherGenesTypeCount[otherGenome.genes[i]]++;
        }

        return Arrays.equals(thisGenesTypeCount, otherGenesTypeCount);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }
}
