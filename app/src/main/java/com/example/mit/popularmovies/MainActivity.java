package com.example.mit.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>,
        SwipeRefreshLayout.OnChildScrollUpCallback {

    static String sortedBy;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;

    final LoaderManager loaderManager = getLoaderManager();

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.movies_grid)
    GridView moviesGrid;
    @BindString(R.string.instance_key_string)
    String SAVE_INSTANCE_KEY;
    @BindString(R.string.msg_no_data_to_load)
    String TOAST_MSG_LOADER_HAS_EMPTY_LIST;
    @BindString(R.string.msg_refreshed)
    String MSG_REFRESHED;
    @BindString(R.string.msg_no_internet_connection)
    String MSG_NO_INTERNET_CONNECTION;
    @BindString(R.string.intent_extra_name)
    String INTENT_EXTRA_NAME;

    private MoviesAdapter adapter;
    private ArrayList<Movie> moviesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            sortedBy = getResources().getString(R.string.sorted_by_popular);
            moviesData = new ArrayList<Movie>();
            initiateLoader();
        } else {
            moviesData = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_KEY);
        }

        adapter = new MoviesAdapter(this, moviesData);
        moviesGrid.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateLoader();
                Log.i(LOG_TAG, MSG_REFRESHED);
            }
        });

        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie selectedMovie = moviesData.get(position);
                openMovieDetail(selectedMovie);
            }
        });
    }

    private void openMovieDetail(Movie selectedMovie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_NAME, selectedMovie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                sortedBy = getResources().getString(R.string.sorted_by_top_rated);
                loaderManager.restartLoader(LOADER_ID, null, this);
                Toast.makeText(this, sortedBy, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, sortedBy);
                return true;
            case R.id.item2:
                sortedBy = getResources().getString(R.string.sorted_by_popular);
                loaderManager.restartLoader(LOADER_ID, null, this);
                Toast.makeText(this, sortedBy, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, sortedBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVE_INSTANCE_KEY, moviesData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        return new MoviesLoader(this, sortedBy);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (movies != null) {
            moviesData.clear();
            adapter.clear();
            moviesData.addAll(movies);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, TOAST_MSG_LOADER_HAS_EMPTY_LIST, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        adapter.clear();
        moviesData.clear();
    }

    private void initiateLoader() {
        if (internetStatus()) {
            loaderManager.initLoader(LOADER_ID, null, this);
            Toast.makeText(this, MSG_REFRESHED, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, MSG_NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, MSG_NO_INTERNET_CONNECTION);
        }
    }

    private boolean internetStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
        return false;
    }

}
