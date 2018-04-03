package com.example.mit.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by mit on 03.04.2018.
 */

public class MoviesLoader extends AsyncTaskLoader<List<String>> {

    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        // TODO: ProgressBard here
    }

    @Override
    public List<String> loadInBackground() {
        List<String> data = Utils.takeMovies();
        return data;
    }
}
