package org.slot.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slot.response.FreeSpinResponse;
import org.slot.response.GameResponse;
import org.slot.response.StartupDataResponse;
import org.slot.service.Game;
import org.slot.service.GameChecker;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.*;


public class GameRequest {
    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/get", new ResultHandler());
        server.createContext("/startup-data", new StartupResultHandler());
        server.start();
    }

    static class ResultHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Game game;
            try {
                game = new Game();
            } catch (Exception e) {
                handleException(httpExchange, e);
                return;
            }

            String query = httpExchange.getRequestURI().getQuery();
            Map<String, String> queryParams = queryToMap(query);
            String cheatType = queryParams.getOrDefault("cheatType", "");
            int betAmount = Integer.parseInt(queryParams.getOrDefault("betAmount", String.valueOf(0)));

            int[][] gameResultMatrix;
            int freeSpins;
            int winRIntValue;
            Map<String, Object> gameResult;
            GameResponse gameResponse = new GameResponse();
            if (cheatType.equals("W")) {
                GameChecker gameChecker = new GameChecker("W");
                while (true) {
                    try {
                        gameResultMatrix = game.gameResultMatrix();
                    } catch (Exception e) {
                        handleException(httpExchange, e);
                        return;
                    }
                    gameResult = gameChecker.checkWin(gameResultMatrix);
                    winRIntValue = (int) gameResult.get("winMultiplier");

                    if (winRIntValue != 0) {
                        gameResponse.setResultMatrix(gameResultMatrix);
                        gameResponse.setWins(gameResult);
                        gameResponse.setWinAmount(winRIntValue * betAmount);
                        break;
                    }

                }
            } else {
                GameChecker gameChecker = new GameChecker(null);
                try {
                    gameResultMatrix = game.gameResultMatrix();
                } catch (Exception e) {
                    handleException(httpExchange, e);
                    return;
                }
                gameResult = gameChecker.checkWin(gameResultMatrix);
                gameResponse.setResultMatrix(gameResultMatrix);
                winRIntValue = (int) gameResult.get("winMultiplier");
                gameResponse.setWins(gameResult);
                gameResponse.setWinAmount(winRIntValue * betAmount);

                try {
                    freeSpins = game.checkFreeSpin(gameResultMatrix);
                } catch (Exception e) {
                    handleException(httpExchange, e);
                    return;
                }

                if (freeSpins > 0) {
                    FreeSpinResponse freeSpinResponse = new FreeSpinResponse();

                    for (int i = 0; i < freeSpins; i++) {
                        try {
                            gameResultMatrix = game.generateFreeSpinResultMatrix();  // Regenerate the matrix
                            gameResult = gameChecker.checkWin(gameResultMatrix);
                            winRIntValue = (int) gameResult.get("winMultiplier");
                            freeSpinResponse.setFreeSpinResult(gameResultMatrix);
                            freeSpinResponse.setFreeSpin(gameChecker.checkWin(gameResultMatrix));
                            freeSpinResponse.setFreeSpinWinAmount(winRIntValue * betAmount);
                            gameResponse.setFreeSpin(freeSpinResponse);

                        } catch (Exception e) {
                            handleException(httpExchange, e);
                            return;
                        }
                    }
                }
            }
            // Convert GameResponse object to JSON string
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(gameResponse);

            // Send HTTP response
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, jsonResponse.length());

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
                os.flush();
            }

        }

        public static Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null || query.isEmpty()) {
                return result;
            }
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
            return result;
        }

        private void handleException(HttpExchange httpExchange, Exception exception) throws IOException {

            int statusCode = 500;
            String message = exception.getMessage();

            String jsonResponse = new Gson().toJson(Collections.singletonMap("error", message));

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(statusCode, jsonResponse.length());

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
                os.flush();
            }

        }

    }


    static class StartupResultHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Game game;

            try {
                game = new Game();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            int[][] gameResultMatrix;
            Map<String, Object> gameResult;
            StartupDataResponse startupDataResponse = new StartupDataResponse();

            System.out.print("this is game responsee" + startupDataResponse);
            int winResult;
            GameChecker gameChecker = new GameChecker(null);
            while (true) {
                try {
                    gameResultMatrix = game.gameResultMatrix();
                    System.out.println("\n");
                    System.out.print("this is game GAMEEEE RESULTTT" + gameResultMatrix);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                gameResult = gameChecker.checkWin(gameResultMatrix);
                winResult = (int) gameResult.get("winMultiplier");

                if (winResult == 0) {
                    System.out.print("SET RESULT MATRIX" + gameResultMatrix);

                    startupDataResponse.setResultMatrix(gameResultMatrix);
                    break;
                }
            }


            Gson gson = new Gson();
            String jsonResponse = gson.toJson(startupDataResponse);

            httpExchange.getResponseHeaders().add("Content-Type", "application");
            httpExchange.sendResponseHeaders(200, jsonResponse.length());

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
                os.flush();
            }

        }


    }
}

