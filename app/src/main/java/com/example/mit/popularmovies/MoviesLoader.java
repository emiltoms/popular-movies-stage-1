package com.example.mit.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    private String sortedBy;

    MoviesLoader(Context context, String sortedBy) {
        super(context);
        this.sortedBy = sortedBy;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        return Utils.takeMovies(sortedBy);
    }
}
