package com.example.mit.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MoviesAdapter extends ArrayAdapter<Movie> {

    private Context context;

    MoviesAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View converterView, @NonNull ViewGroup parent) {
        View listItemView = converterView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, parent, false);
        }

        Movie currentMovie = (Movie) getItem(position);

        ImageView imageView = listItemView.findViewById(R.id.image_view_item_grid);
        assert currentMovie != null : context.getString(R.string.adapter_error_currentMovie_is_null);
        Picasso.with(context).load(currentMovie.getThumbnail()).into(imageView);

        return listItemView;
    }

}
