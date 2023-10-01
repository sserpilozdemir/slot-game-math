package org.slot.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class RNGService {

    OkHttpClient client = new OkHttpClient();

    public RNGService() {
    }


    public long rngService(int amount) {
        String url = "http://10.29.39.220:3005/rng?amount=" + amount;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonArray1 = response.body().string();
            JSONObject jsonObj = new JSONObject(jsonArray1);

            JSONArray jsonArray = jsonObj.getJSONArray("rng");
            long seed = jsonArray.getLong(0);
            return seed;

        } catch (SocketTimeoutException ste) {
            System.err.println("Socket timed out when connecting to RNG service");
            ste.printStackTrace();
            throw new RuntimeException("Connection timed out when accessing the RNG service", ste);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IOException error connecting to RNG service.", e);
        }
    }

}