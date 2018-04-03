package com.example.mit.popularmovies;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.movies_grid)
    GridView moviesGrid;

    @BindView(R.id.testing_lv)
    ListView testingListView;

    @BindView(R.id.total_results_tv)
    TextView mainTextView;

    ArrayAdapter<String> adapter;
//    List<String> results;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Bind ButterKnife library to this activity
        ButterKnife.bind(this);
        Log.e(LOG_TAG, "ButterKnife is binded.");
        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        Log.e(LOG_TAG, "Picasso picture is loaded.");

//        ArrayList<String> results = Utils.takeMovies();
//        Log.e(LOG_TAG, " results = Utils.takeMovies(): " + results);

//        String[] testArray = getResources().getStringArray(R.array.testing_array_string);
//        Log.e(LOG_TAG, " testArray = getResources().getStringArray(R.array.testing_array_string): " + testArray);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        Log.e(LOG_TAG, " ArrayAdapter - STARTED: " + adapter);
//
//        results.addAll(Utils.takeMovies());
//
        moviesGrid.setAdapter(adapter);
//        testingListView.setVisibility(View.GONE);
//        testingListView.setAdapter(adapter);

        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int i, Bundle bundle) {
        return new MoviesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        // TODO: ProgressBar GONE
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            Toast.makeText(this, "Error. No data to load.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        adapter.clear();
    }
}


























