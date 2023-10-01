package org.slot.response;

import java.util.Map;

public class FreeSpinResponse {

    private  int[][] freeSpinResult;

    private Map<String, Object> freeSpin;

    private int freeSpinWinAmount;

    public void setFreeSpinResult(int[][] freeSpinResult) {
        this.freeSpinResult = freeSpinResult;
    }

    public void setFreeSpin(Map<String, Object> freeSpin) {
        this.freeSpin = freeSpin;
    }

    public void setFreeSpinWinAmount(int freeSpinWinAmount) {
        this.freeSpinWinAmount = freeSpinWinAmount;
    }
}
