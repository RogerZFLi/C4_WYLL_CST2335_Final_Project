package com.example.amusu.teamname_cst2335_final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import com.example.amusu.teamname_cst2335_final_project.movie.Movie;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieContract;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieDetailActivity;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieHelper;
import com.example.amusu.teamname_cst2335_final_project.movie.MovieListAdapter;

/**
 * Activity to search movie title and list saved movies.
 * A movie summary is also at the bottom.
 */
public class MovieInformationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, Button.OnClickListener {
    private static final String TAG = "MovieInformation";
    private static final int MOVIE_LOADER_ID = 0;
    //Search filter working with OnQueryTextListener
    private Toolbar movieToolBar; //set variable for movie toolbar
    private String mCurFilter;      //set variable for movie search filter
    private SearchView mSearchView; //set variable for search view
    private ListView lvMovieList;   // set movie list variable
    private TextView tvShortestRuntime;  // shortest runtime for searched movies
    private TextView tvAverageRuntime;   // average runtime for searched movies
    private TextView tvLongestRuntime;  // longest runtime for searched movies
    private CursorAdapter mAdapter;     // set variable for cursor adapter
    private Button movieBtn;            // movie search button
    private Button backtomainBtn;       // back to main menu button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Log.d(TAG, "onActivityCreated");

        movieToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolBar); //set Toolbar.
        getSupportActionBar().setTitle("Menu ->");

        movieBtn= (Button)findViewById(R.id.moviesearchButton);
        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Enter movie name to search ", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        backtomainBtn= (Button)findViewById(R.id.backToMainBtn);
        backtomainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieInformationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mSearchView = findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);

        tvShortestRuntime = findViewById(R.id.shortest_runtime);
        tvAverageRuntime =  findViewById(R.id.average_runtime);
        tvLongestRuntime =  findViewById(R.id.longest_runtime);
        lvMovieList = findViewById(R.id.lv_movie_list);

        mAdapter = new MovieListAdapter(this, null, 0);
        lvMovieList.setAdapter(mAdapter);
        lvMovieList.setOnItemClickListener(this);
        lvMovieList.setOnItemLongClickListener(this);

        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true;
    }
    /*
        This function is used to set the items in the menu.
         */

    public boolean onOptionsItemSelected(MenuItem mItem) {
        switch (mItem.getItemId()) {

            case R.id.movie_movie:
//                Intent intent = new Intent(MovieInformationActivity.this, MainActivity.class);
//                startActivity(intent);
                android.support.v7.app.AlertDialog.Builder alertBuilder1 = new android.support.v7.app.AlertDialog.Builder(this);
                alertBuilder1.setTitle("You are already in Movie session.");
                alertBuilder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                // Create the AlertDialog
                android.support.v7.app.AlertDialog dialog1 = alertBuilder1.create();
                dialog1.show();
                break;
            case R.id.movie_bus:
                Intent intent1 = new Intent(MovieInformationActivity.this, OCTranspoBusRouteActivity.class);
                startActivity(intent1);
                break;
            case R.id.movie_cbc:
                Intent intent2 = new Intent(MovieInformationActivity.this, CBCNewsReaderActivity.class);
                startActivity(intent2);
                break;
            case R.id.movie_food:
                Intent intent3 = new Intent(MovieInformationActivity.this, FoodNutritionDatabaseActivity.class);
                startActivity(intent3);
                break;
            case R.id.movie_about:
                //Toast.makeText(this,R.string.aboutInfo, Toast.LENGTH_SHORT).show();
                android.support.v7.app.AlertDialog.Builder alertBuilder2 = new android.support.v7.app.AlertDialog.Builder(this);
                alertBuilder2.setTitle(R.string.aboutInfo);
                alertBuilder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                // Create the AlertDialog
                android.support.v7.app.AlertDialog dialog2 = alertBuilder2.create();
                dialog2.show();
                break;
        }
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *  <p/>Note: create a loader for query all movies
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader for loader id:" + id);

        switch (id) {
            case MOVIE_LOADER_ID:
                return new CursorLoader(this, MovieContract.Movies.CONTENT_URI, MovieHelper.getProjection(), null, null, null);
            default:
                throw new IllegalArgumentException("Invalid loader id");
        }
    }

    /**
     * <p/>Note:populate the listview and update movie summary
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished for loader id:" + loader.getId());

        switch (loader.getId()) {
            case MOVIE_LOADER_ID:
                this.mAdapter.swapCursor(data);
                updateSummary(data);
                break;
            default:
                Log.w(TAG, "unhandled loader id " + loader.getId());
                break;
        }
    }

    /**
     * {@inheritDoc}
     * <p/>Note:also reset movie summary
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset for loader id:" + loader.getId());
        //this.showProgressbar();
        switch (loader.getId()) {
            case MOVIE_LOADER_ID:
                this.mAdapter.swapCursor(null);
                updateSummary(null);
                break;
            default:
                Log.w(TAG, "unhandled loader id " + loader.getId());
                break;
        }
    }

    /**
     * update movie summary, including shortest, average, and longest runtime.
     * @param data the cursor
     */
    private void updateSummary(Cursor data) {
        int longest_pos = -1;
        int shortest_pos = -1;
        int longest = -1;
        int shortest = 10000;
        long total_runtime = 0;
        int movie_count = 0;
        if (data == null) {
            tvLongestRuntime.setText("");
            tvShortestRuntime.setText("");
            tvAverageRuntime.setText("");
            return;
        }
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            int runtime = data.getInt(MovieHelper.MOVIE_RUNTIME_IDX);
            if (runtime == 0){
                continue;
            }
            if (runtime > longest){
                longest = runtime;
                longest_pos = data.getPosition();
            }
            if (runtime < shortest){
                shortest = runtime;
                shortest_pos = data.getPosition();
            }

            total_runtime += runtime;
            movie_count ++;
        }

        if (longest_pos == -1){
            tvLongestRuntime.setText("");
        } else {
            data.moveToPosition(longest_pos);
            Movie m = MovieHelper.fromCursor(data);
            String s = getString(R.string.runtime_summary, m.getRuntime(), m.getTitle(), m.getYear());
            tvLongestRuntime.setText(s);
        }

        if (shortest_pos == -1){
            tvShortestRuntime.setText("");
        } else {
            data.moveToPosition(shortest_pos);
            Movie m = MovieHelper.fromCursor(data);
            String s = getString(R.string.runtime_summary, m.getRuntime(), m.getTitle(), m.getYear());
            tvShortestRuntime.setText(s);
        }

        if (total_runtime == 0 || movie_count == 0) {
            tvAverageRuntime.setText("");
        } else {
            long avg = total_runtime / movie_count;
            String s = getString(R.string.runtime_average, avg);
            tvAverageRuntime.setText(s);
        }
    }

    /**
     * {@inheritDoc}
     * <p/>Note:search omdb for movie title
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurFilter = !TextUtils.isEmpty(query) ? query.trim() : null;
        if(TextUtils.isEmpty(mCurFilter)){
            return true;
        }
        Log.d(TAG, "movie search " + mCurFilter);

        Intent i = new Intent(this, MovieDetailActivity.class);
        i.putExtra("ID", -1L);
        i.putExtra("TITLE", mCurFilter);
        startActivity(i);
        return true;
    }

    //This is a Searchview callback. Restart the loader.
    //This gets called when user enters new search text.
    //Call LoaderManager.restartLoader to trigger the onCreateLoader

    /**
     * {@inheritDoc}
     * <p/>Note: get the search keywords
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed. Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        //Log.d(TAG, "Restarting the loader");
        //getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>Note: Long click to delete the movie
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "long click id" + id);
        final long movie_id = id;
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        if (cursor == null) {
            return true;
        }
        final String title =  cursor.getString(MovieHelper.MOVIE_TITLE_IDX);
        Log.d(TAG, "movie id " + id + ", title " + title);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        MovieHelper.delMovie(MovieInformationActivity.this, movie_id);
                        String msg = MovieInformationActivity.this.getString(R.string.movie_deleted, title);
                        Toast toast = Toast.makeText(MovieInformationActivity.this, msg, Toast.LENGTH_SHORT);
                        toast.show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msg = getString(R.string.movie_confirm_delete, title);
        builder.setMessage(msg).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();

        return true;
    }

    /**
     * {@inheritDoc}
     * <p/>Note: single click to show movie information
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "click id" + id);
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        String title = cursor.getString(MovieHelper.MOVIE_TITLE_IDX);
        Log.d(TAG, "movie id " + id + ", title " + title);
        Intent i = new Intent(this, MovieDetailActivity.class);
        i.putExtra("ID", id);
        i.putExtra("TITLE", title);
        startActivity(i);
    }

    /**
     * {@inheritDoc}
     * <p/>Note: display help dialog
     */
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.help);
        builder1.setMessage(R.string.movie_help_message);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}


