package org.slot.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;


public class GameChecker {
    private int goldenWildChar = 9; // Replace with actual value
    private final static Map<String, List<int[]>> groupWinLines = new HashMap<>();
    private final static Map<String, int[]> payTable = new HashMap<>();

    static {
        loadFromJson();
    }

    private static void loadFromJson() {
        InputStream inputStream = GameChecker.class.getClassLoader().getResourceAsStream("data.json");
        Scanner scanner = new Scanner(inputStream);
        String content = scanner.useDelimiter("\\A").next();
        scanner.close();

        JSONObject json = new JSONObject(content);

        JSONObject jsonGroupWinLines = json.getJSONObject("groupWinLines");
        for (String key : jsonGroupWinLines.keySet()) {
            JSONArray lineArray = jsonGroupWinLines.getJSONArray(key);
            List<int[]> lines = new ArrayList<>();
            for (int i = 0; i < lineArray.length(); i++) {
                JSONArray line = lineArray.getJSONArray(i);
                int[] intLine = new int[line.length()];
                for (int j = 0; j < line.length(); j++) {
                    intLine[j] = line.getInt(j);
                }
                lines.add(intLine);
            }
            groupWinLines.put(key, lines);
        }

        JSONObject jsonPayTable = json.getJSONObject("payTable");
        for (String key : jsonPayTable.keySet()) {
            JSONArray array = jsonPayTable.getJSONArray(key);
            int[] values = new int[array.length()];
            for (int i = 0; i < array.length(); i++) {
                values[i] = array.getInt(i);
            }
            payTable.put(key, values);
        }

    }

    public GameChecker(String cheatType) {
        System.out.println("takennnnnnn");
    }


    public Map<String, Object> checkWin(int[][] mainMatrix) {
        System.out.println("\n");
        System.out.println("........:::Checking win amount:::.........");
        System.out.println("\n");

        List<Integer> matrix = new ArrayList<>();
        for (int col = 0; col < mainMatrix[0].length; col++) {
            for (int row = 0; row < mainMatrix.length; row++) {
                matrix.add(mainMatrix[row][col]);
            }
        }

        List<List<Integer>> winLines = new ArrayList<>();
        List<String> winSymbol = new ArrayList<>();
        int winMultiplier = 0;

        for (String key : groupWinLines.keySet()) {
            String[] keyParts = key.split(",");
            int firstColumnSym = matrix.get(Integer.parseInt(keyParts[0]) - 1);
            int secondColumnSym = matrix.get(Integer.parseInt(keyParts[1]) - 1);
            int sym = (firstColumnSym == secondColumnSym)
                    ? firstColumnSym
                    : (firstColumnSym == goldenWildChar)
                    ? secondColumnSym
                    : (secondColumnSym == goldenWildChar)
                    ? firstColumnSym : 0;

            if (sym != 0) {
                int counter = 2;
                List<Integer> winLine = new ArrayList<>(Arrays.asList(sym, Integer.parseInt(keyParts[0]), Integer.parseInt(keyParts[1])));
                for (int i = 0; i < groupWinLines.get(key).size(); i++) {
                    List<int[]> line = groupWinLines.get(key);
                    while (line.get(i).length > counter && (sym == matrix.get(line.get(i)[counter] - 1) || goldenWildChar == matrix.get(line.get(i)[counter] - 1))) {
                        winLine.add(line.get(i)[counter]);
                        counter++;
                    }
                }


                if (winLine.size() > 3) {
                    winLines.add(winLine);
                    winMultiplier += payTable.get(String.valueOf(sym))[counter - 1];
                    if (!winSymbol.contains(String.valueOf(sym))) {
                        winSymbol.add(String.valueOf(sym));
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("winMultiplier", winMultiplier);
        result.put("winLines", winLines);
        result.put("winSymbol", winSymbol);
        System.out.print("this is game result" + result);
        System.out.println("\n");

        return result;
    }
}