package org.slot;

import org.slot.controller.GameRequest;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        //spin
        GameRequest gameRequest = new GameRequest();
        gameRequest.startServer();


    }

}



