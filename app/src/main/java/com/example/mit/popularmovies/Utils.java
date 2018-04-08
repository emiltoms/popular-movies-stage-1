package com.example.mit.popularmovies;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final String LOG_MSG_EXCEPTION = "Catch: JSONException e ==";
    private static final String LOG_MSG_JSON_EXTRACTED
            = "JSON objects has been extracted to ArrayList<Movie> movies ==";
    private static final String LOG_MSG_RESPONSE_CODE = "HttpURLConnection Response code:";

    private static final String NO_DATA = "n/a";
    private static final int NO_DATA_INT = -1;

    private static final String RESULTS_JSON_ARRAY = "results";
    private static final String ID_JSON = "id";
    private static final String TITLE_JSON = "title";
    private static final String RELEASE_DATE_JSON = "release_date";
    private static final String OVERVIEW_JSON = "overview";
    private static final String VOTE_AVERAGE_JSON = "vote_average";
    private static final String POSTER_PATH_JSON = "poster_path";

    private static final String API_KEY = "000"; // TODO: Put your Api key here
    private static final String POSTER_PATH_LINK_BASE = "http://image.tmdb.org/t/p/w185";

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
            Log.e(LOG_TAG, LOG_MSG_RESPONSE_CODE + responseCode);
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readStream(inputStream);
                Log.e(LOG_TAG, LOG_MSG_RESPONSE_CODE + urlConnection.getResponseCode());
            } else {
                Log.e(LOG_TAG, LOG_MSG_RESPONSE_CODE + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, LOG_MSG_EXCEPTION + e);
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

    public static List<Movie> takeMovies(String sortedBy) {

        String newUrl = createURL(sortedBy).toString();
        Log.wtf(LOG_TAG, "Created url: " + newUrl);
        String jsonResponse = null;

        try {
            URL url = new URL(newUrl);
            jsonResponse = httpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException error: " + e);
        }

        return extractMovies(jsonResponse);
    }

    private static ArrayList<Movie> extractMovies(String movieJSON) {
        // String movieJSON can't be empty in next steps, check it right here:
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        int movieID;
        String thumbnail;
        String titleMovie;
        String releaseDate;
        String movieOverview;
        String rating;
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(movieJSON);
            JSONArray results = jsonObject.getJSONArray(RESULTS_JSON_ARRAY);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                if (movie.has(ID_JSON)) {
                    movieID = movie.getInt(ID_JSON);
                } else {
                    movieID = NO_DATA_INT;
                }

                if (movie.has(TITLE_JSON)) {
                    titleMovie = movie.getString(TITLE_JSON);
                } else {
                    titleMovie = NO_DATA;
                }

                if (movie.has(RELEASE_DATE_JSON)) {
                    releaseDate = movie.getString(RELEASE_DATE_JSON);
                } else {
                    releaseDate = NO_DATA;
                }

                if (movie.has(OVERVIEW_JSON)) {
                    movieOverview = movie.getString(OVERVIEW_JSON);
                } else {
                    movieOverview = NO_DATA;
                }

                if (movie.has(VOTE_AVERAGE_JSON)) {
                    rating = String.valueOf(movie.getDouble(VOTE_AVERAGE_JSON));
                } else {
                    rating = NO_DATA;
                }

                if (movie.has(POSTER_PATH_JSON)) {
                    thumbnail = createPosterURL(movie.getString(POSTER_PATH_JSON));
                } else {
                    thumbnail = NO_DATA;
                }

                // Add movie with new data to list
                Movie item = new Movie(movieID, thumbnail, titleMovie, releaseDate, movieOverview, rating);
                movies.add(item);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, LOG_MSG_EXCEPTION + e);
        }
        Log.e(LOG_TAG, LOG_MSG_JSON_EXTRACTED + movies);
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

    private static Uri createURL(String sortedBy) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sortedBy)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", "en-US");

        return builder.build();
    }

    private static String createPosterURL(String posterPath) {
        return POSTER_PATH_LINK_BASE + posterPath;
    }
}

