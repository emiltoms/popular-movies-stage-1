package com.example.mit.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private Movie selectedMovie;

    @BindView(R.id.thumbnail_image)
    ImageView thumbnailIV;
    @BindView(R.id.title_tv)
    TextView titleTV;
    @BindView(R.id.release_date_tv)
    TextView releaseDateTV;
    @BindView(R.id.movie_overview_tv)
    TextView movieOverviewTV;
    @BindView(R.id.rating_tv)
    TextView ratingTV;
    @BindString(R.string.msg_null_pointer_exception)
    String MSG_NULL_EXCEPTION;
    @BindString(R.string.intent_extra_name)
    String INTENT_EXTRA_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        try {
            selectedMovie = getIntent().getExtras().getParcelable(INTENT_EXTRA_NAME);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, MSG_NULL_EXCEPTION + e);
            closeActivity();
        }

        Picasso.with(this).load(selectedMovie.getThumbnail()).into(thumbnailIV);

        titleTV.setText(selectedMovie.getTitleMovie());
        releaseDateTV.setText(selectedMovie.getReleaseDate());
        movieOverviewTV.setText(selectedMovie.getMovieOverview());
        ratingTV.setText(selectedMovie.getRating());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void closeActivity() {
        finish();
    }
}
