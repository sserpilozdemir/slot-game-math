package org.slot.util;

public class ReelGenerator {

    private String cheatType;


    public int[] generateReel(int randomNumber, int[] oneReel, int visible ){
        int[] oneReelLine = new int[visible];
        for(int i = 0; i <oneReelLine.length; i++){
            oneReelLine[i] = oneReel[(randomNumber + i) % oneReel.length];
        }
        return oneReelLine;
    }



}
