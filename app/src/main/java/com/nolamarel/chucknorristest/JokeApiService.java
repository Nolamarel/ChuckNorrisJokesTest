package com.nolamarel.chucknorristest;

import com.google.gson.Gson;
import com.nolamarel.chucknorristest.models.JokeApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JokeApiService {
    private static final String API_URL = "https://api.chucknorris.io/jokes/random";
    public static JokeApi getJoke() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return new Gson().fromJson(result.toString(), JokeApi.class);
            } else {
                throw new IOException("API request failed: " + connection.getResponseCode());
            }

        } finally {
            connection.disconnect();
        }
    }
}
