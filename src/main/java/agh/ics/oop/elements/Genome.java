package agh.ics.oop.elements;

import agh.ics.oop.utility.Ensure;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Genome {
    private final int[] genes;

    public Genome() {
        this(ThreadLocalRandom.current().ints(32, 0, 8).toArray());
    }

    public Genome(int[] genes) {
        Ensure.Not.Null(genes, "genome's genes");
        if (genes.length != 32)
            throw new IllegalArgumentException("'genes' argument length should be 32");

        this.genes = genes;
        Arrays.sort(this.genes);
    }

    public int getRandomGene() {
        return genes[ThreadLocalRandom.current().nextInt(genes.length)];
    }

    public Genome crossoverWith(Genome otherGenome, float genesRatio) {
        Ensure.Not.Null(otherGenome, "other genome");
        Ensure.Is.InRange(genesRatio, 0f, 1f, "genes ratio");

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

        return Arrays.equals(genes, otherGenome.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }
}
