package org.slot.service;

import org.slot.util.RandomNumbGenerate;
import org.slot.util.ReelGenerator;

import java.util.Arrays;

public class Game {

    int[][] allReels = {
            {3, 7, 2, 10, 1, 11, 5, 8, 6, 9, 7, 10, 3, 11, 5, 6, 8, 2, 4, 1, 9, 10, 3, 11, 7, 2, 5, 6, 8, 9, 1, 4},
            {11, 5, 6, 1, 3, 7, 9, 2, 4, 10, 8, 7, 2, 1, 6, 5, 9, 3, 4, 11, 7, 2, 8, 10, 3, 6, 1, 9, 4, 5, 11, 7, 2, 6, 3, 8, 10, 9, 4, 5, 1, 11, 2, 7, 8},
            {1, 7, 5, 2, 6, 9, 3, 10, 8, 4, 11, 7, 6, 2, 3, 1, 9, 5, 10, 8, 4, 11, 7, 2, 3, 6, 9, 1, 4, 10, 5, 8, 11, 7},
            {2, 1, 7, 5, 6, 3, 8, 4, 9, 10, 11, 7, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 7, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 7, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 7, 1, 2, 3, 4, 5, 6, 8},
            {5, 6, 7, 8, 1, 2, 3, 4, 9, 10, 11, 7, 6, 2, 3, 1, 4, 5, 8, 9, 10, 11, 7, 2, 1, 3, 4, 6, 5, 8, 9, 10, 11, 7, 6, 2, 3, 1, 4}
    };

    int[][] freeSpinReels = {

            {5, 7, 2, 10, 1, 9, 3, 8, 6, 4, 7, 10, 3, 5, 6, 8, 2, 4, 1, 9, 10, 7, 2, 5, 6, 8, 9, 1, 4, 3, 5, 7, 2, 8, 6, 9, 10, 1, 4, 3, 7},
            {8, 5, 6, 1, 3, 7, 9, 2, 4, 10, 3, 7, 2, 1, 6, 5, 9, 8, 4, 10, 7, 2, 3, 6, 9, 1, 4, 10, 5, 8, 6, 2, 3, 4, 9, 1, 7, 5, 8},
            {2, 6, 9, 1, 5, 8, 4, 10, 7, 3, 2, 6, 9, 1, 4, 7, 5, 8, 3, 10, 2, 6, 9, 1, 5, 8, 4, 7, 3, 10, 2, 6, 9, 1, 4, 7, 5, 8, 3, 10, 2, 6, 9, 1, 5, 8, 4, 7},
            {3, 7, 2, 5, 1, 4, 8, 6, 9, 10, 2, 5, 8, 1, 4, 7, 3, 6, 9, 10, 3, 7, 2, 5, 1, 4, 8, 6, 9, 10, 2, 5, 8, 1, 4, 7, 3, 6, 9, 10, 3, 7, 2, 5, 1, 4, 8, 6, 9, 10, 3},
            {10, 4, 8, 5, 1, 9, 6, 3, 7, 2, 4, 8, 1, 5, 9, 2, 6, 3, 7, 10, 4, 8, 5, 1, 9, 6, 3, 7, 2, 4, 8, 1, 5, 9, 6, 3, 7, 2}
    };

    private int scatter  = 11;

    private int[][] gameResultMatrix;

    private int[] generatedRandoms;

    RandomNumbGenerate randomNumbGenerate = new RandomNumbGenerate();


    public int[] generateAllRandoms(int[][] allReels) throws Exception {
        int[] allRandoms = new int[allReels.length];;
        for(int i = 0; i < allReels.length; i++) {
            allRandoms[i] = randomNumbGenerate.randomNumGenerate(allReels[i]);
        }
        return allRandoms;
    }


    public int[][] gameResultMatrix() throws Exception {

        gameResultMatrix = new int[allReels.length][];

        generatedRandoms = generateAllRandoms(allReels);
        System.out.println("this is generated randoms" + Arrays.toString(generatedRandoms));


        ReelGenerator reelGenerator = new ReelGenerator();
        for(int i = 0; i < allReels.length; i ++){
            gameResultMatrix[i] = reelGenerator.generateReel(generatedRandoms[i], allReels[i], 4);
        }

        System.out.print("this is last resultMatrix"  + Arrays.deepToString(gameResultMatrix));
        return gameResultMatrix;
    }

    public int checkFreeSpin(int[][] resultMatrix) throws Exception {
        Game game = new Game();
//        resultMatrix = game.gameResultMatrix();
        int[] mergedArray = Arrays.stream(resultMatrix)
                .flatMapToInt(Arrays::stream)
                .toArray();

        int counter = 0;
        for(int i = 0; i < mergedArray.length; i++) {
            if(mergedArray[i] == scatter) {
                counter++;
            }
        }
        return counter;
    }

    public int[][] generateFreeSpinResultMatrix() throws Exception {

        gameResultMatrix = new int[freeSpinReels.length][];

        generatedRandoms = generateAllRandoms(freeSpinReels);

        System.out.println("this is generated randoms" + Arrays.toString(generatedRandoms));


        ReelGenerator reelGenerator = new ReelGenerator();
        for(int i = 0; i < freeSpinReels.length; i ++){
            gameResultMatrix[i] = reelGenerator.generateReel(generatedRandoms[i], freeSpinReels[i], 4);
        }

        System.out.print("this is last resultMatrix"  + Arrays.deepToString(gameResultMatrix));
        return gameResultMatrix;

    }

}
