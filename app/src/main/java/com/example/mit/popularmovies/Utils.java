package com.example.mit.popularmovies;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mit on 24.03.2018.
 */

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final String noDataString = "n/a";

    public Utils() {
    }

    private static String httpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            Log.e(LOG_TAG, "response code: " + responseCode);
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readStream(inputStream);
                Log.e(LOG_TAG, "Response code: " + urlConnection.getResponseCode());
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException caught in 'httpRequest' method: " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static List<String> takeMovies() {

        String newUrl = createURL().toString();
        Log.e(LOG_TAG, "Created url: " + newUrl);
        String jsonResponse = null;

        try {
            URL url = new URL(newUrl);
            jsonResponse = httpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException error: " + e);
        }

        return extractMovies(jsonResponse);
    }

    @Nullable
    public static ArrayList<String> extractMovies(String movieJSON) {
        String titleMovie;
        ArrayList<String> movies = new ArrayList<>();

        // String movieJSON can't be empty in next steps, check it right here:
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(movieJSON);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                if (movie.has("title")) {
                    titleMovie = movie.getString("title");
                } else {
                    titleMovie = noDataString;
                }


                // Add movie to list
                movies.add(titleMovie);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "ERROR! Problem with JSON String extracting! Error: " + e);
        }
        Log.e(LOG_TAG, "extractMovies return with: " + movies);
        return movies;
    }

    /**
     * this method transforms JSON input stream into String by StringBuilder and InputStreamReader
     */
    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static Uri createURL() {
        String apiKey = "";// TODO: api key here

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("top_rated")
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "en-US");

        return builder.build();
    }
}




























