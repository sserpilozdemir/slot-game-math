package org.slot.util;
import org.apache.commons.math3.random.MersenneTwister;

public class RandomNumbGenerate {
    private Long seed;

    public Long getSeed() {
        RNGService rngService = new RNGService();
        try {
            seed = rngService.rngService(1);
            System.out.println("Received seed: " + seed);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());

        }
        return seed;
    }

    public int randomNumGenerate(int[] reelline) {
        MersenneTwister randomGenerator = new MersenneTwister(getSeed());
        int randomNumber = randomGenerator.nextInt(reelline.length);
        return randomNumber;


    }

}
