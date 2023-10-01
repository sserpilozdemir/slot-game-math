package org.slot.response;

import java.util.Map;

public class GameResponse {

    private int[][] resultMatrix;

    private Map<String, Object> wins;

    private int winAmount;
    FreeSpinResponse freeSpin = new FreeSpinResponse();

    public void setResultMatrix(int[][] resultMatrix) {
        this.resultMatrix = resultMatrix;
    }

    public void setWins(Map<String, Object> wins) {
        this.wins = wins;
    }


    public void setWinAmount(int winAmount) {
        this.winAmount = winAmount;
    }

    public void setFreeSpin(FreeSpinResponse freeSpinResponse) {
        this.freeSpin = freeSpinResponse;
    }

}