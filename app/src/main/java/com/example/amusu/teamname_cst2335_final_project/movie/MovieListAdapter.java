package com.example.amusu.teamname_cst2335_final_project.movie;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amusu.teamname_cst2335_final_project.R;

import java.io.File;

/**
 * a cursor adaptor for movie list view.
 */
public class MovieListAdapter extends CursorAdapter {
    private static final String TAG = "MovieListAdapter";

    public MovieListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView ivIcon = view.findViewById(R.id.movie_list_item_thumbnail);
        TextView tvTitle = view.findViewById(R.id.movie_list_item_title);
        TextView tvYear = view.findViewById(R.id.movie_list_item_year);

        Movie movie = MovieHelper.fromCursor(cursor);
        view.setTag(movie);

        tvTitle.setText(movie.getTitle());
        tvYear.setText(movie.getYear());
        try {
            File f = context.getFileStreamPath(movie.getThumbnailFile());
            ivIcon.setImageURI(Uri.fromFile(f));
            //Log.d(TAG, "load thumbnail from " + f.getAbsolutePath());
        }catch(Exception e){
            //Log.w(TAG, "failed to load thumbnail", e);
            ivIcon.setImageURI(null);
            ivIcon.setMinimumHeight(40);
            ivIcon.setMinimumWidth(40);
        }
    }
}
