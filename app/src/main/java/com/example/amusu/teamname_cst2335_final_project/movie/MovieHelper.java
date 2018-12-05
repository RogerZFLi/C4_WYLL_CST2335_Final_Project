package com.example.amusu.teamname_cst2335_final_project.movie;


import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;

/**
 * A help class for {@link Movie} in ORM.
 */
public class MovieHelper {
    //Movie columns that will be retrieved
    private static final String[] PROJECTION = new String[]{
            MovieContract.Movies._ID,
            MovieContract.Movies.TITLE,
            MovieContract.Movies.YEAR,
            MovieContract.Movies.IMDB_RATING,
            MovieContract.Movies.PLOT,
            MovieContract.Movies.POSTER,
            MovieContract.Movies.POSTER_FILE,
            MovieContract.Movies.POSTER_THUMBNAIL,
            MovieContract.Movies.DESCRIPTION,
            MovieContract.Movies.RUNTIME,
            MovieContract.Movies.ACTORS
    };

    public static final int MOVIE_ID_IDX = 0;
    public static final int MOVIE_TITLE_IDX = 1;
    public static final int MOVIE_YEAR_IDX = 2;
    public static final int MOVIE_IMDB_RATING_IDX = 3;
    public static final int MOVIE_PLOT_IDX = 4;
    public static final int MOVIE_POSTER_IDX = 5;
    public static final int MOVIE_POSTER_FILE_IDX = 6;
    public static final int MOVIE_POSTER_THUMBNAIL_IDX = 7;
    public static final int MOVIE_DESCRIPTION_IDX = 8;
    public static final int MOVIE_RUNTIME_IDX = 9;
    public static final int MOVIE_ACTORS_IDX = 10;
    private static final String TAG = "MovieHelper";

    /**
     * This utility class cannot be instantiated
     */
    private MovieHelper() {
        // do nothing
    }

    /**
     * Get the {@link com.example.amusu.teamname_cst2335_final_project.movie} projection.
     *
     * @return the project
     */
    public static String[] getProjection() {
        //return PROJECTION.clone();
        return  Arrays.copyOf(PROJECTION, PROJECTION.length);
    }

    /**
     * Construct a new {@link Movie} object from {@link Cursor}.
     *
     * @param cursor the cursor
     * @return the new {@link Movie} object
     */
    public static Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();
        try {
            long id = cursor.getLong(MOVIE_ID_IDX);
            String title = cursor.getString(MOVIE_TITLE_IDX);
            String year = cursor.getString(MOVIE_YEAR_IDX);
            String rating = cursor.getString(MOVIE_IMDB_RATING_IDX);
            String plot = cursor.getString(MOVIE_PLOT_IDX);
            String poster = cursor.getString(MOVIE_POSTER_IDX);
            String poster_file = cursor.getString(MOVIE_POSTER_FILE_IDX);
            String thumbnail = cursor.getString(MOVIE_POSTER_THUMBNAIL_IDX);
            String description = cursor.getString(MOVIE_DESCRIPTION_IDX);
            String actors = cursor.getString(MOVIE_ACTORS_IDX);
            int runtime = cursor.getInt(MOVIE_RUNTIME_IDX);

            movie.setId(id);
            movie.setTitle(title);
            movie.setYear(year);
            movie.setRating(rating);
            movie.setPlot(plot);
            movie.setPoster(poster);
            movie.setPosterFile(poster_file);
            movie.setThumbnailFile(thumbnail);
            movie.setDescription(description);
            movie.setRuntime(runtime);
            movie.setActors(actors);
        } catch (Exception e) {
            Log.w(TAG, "failed to convert to movie", e);
        }
        return movie;
    }

    /**
     * Constructs a {@link Movie} object from id.
     *
     * @param context the context
     * @param id the movie id
     * @return the Movie represents the id or null
     */
    public static Movie getMovie(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.Movies.CONTENT_URI,
                PROJECTION,
                "_id = ?",
                new String[]{Long.toString(id)},
                null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Constructs a {@link Movie} object from title.
     *
     * @param context the context
     * @param title the movie title
     * @return the Movie represents the title or null
     */
    public static Movie getMovie(Context context, String title) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.Movies.CONTENT_URI,
                PROJECTION,
                "title = ?",
                new String[]{title},
                null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * finds the shortest runtime {@link Movie} object.
     *
     * @param context the context
     * @return the Movie the shortest runtime movie or null
     */
    public static Movie getShortestMovie(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.Movies.CONTENT_URI,
                PROJECTION,
                null,
                null,
                "runtime ASC");
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * finds the longest runtime {@link Movie} object.
     *
     * @param context the context
     * @return the Movie  the longest runtime movie or null
     */
    public static Movie getLongestMovie(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.Movies.CONTENT_URI,
                PROJECTION,
                null,
                null,
                "runtime DESC");
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Gets the average {@link Movie} runtime.
     *
     * @param context the context
     * @return the average runtime
     */
    public static int getAverageMovieRuntime(Context context) {
        ContentProviderClient client =  context.getContentResolver().acquireContentProviderClient(MovieContract.AUTHORITY);
        SQLiteDatabase dbHandle= ((MovieProvider)client.getLocalContentProvider()).getDatabase();
        SQLiteStatement stmt = dbHandle.compileStatement("SELECT AVERAGE(runtime) FROM movie");
        try {
            return (int) stmt.simpleQueryForLong();
        }catch(Exception e){
            return 0;
        }finally {
            stmt.close();
            client.close();
        }
    }

    /**
     * add a movie into database
     * @param context the context
     * @param movie the movie object
     * @return the movie uri
     */
    public static Uri addMovie(Context context, Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.Movies.TITLE, movie.getTitle());
        cv.put(MovieContract.Movies.YEAR, movie.getYear());
        cv.put(MovieContract.Movies.ACTORS, movie.getActors());
        cv.put(MovieContract.Movies.IMDB_RATING, movie.getRating());
        cv.put(MovieContract.Movies.PLOT, movie.getPlot());
        cv.put(MovieContract.Movies.RUNTIME, movie.getRuntime());
        cv.put(MovieContract.Movies.POSTER, movie.getPoster());
        cv.put(MovieContract.Movies.POSTER_FILE, movie.getPosterFile());
        cv.put(MovieContract.Movies.POSTER_THUMBNAIL, movie.getThumbnailFile());
        cv.put(MovieContract.Movies.DESCRIPTION, movie.getDescription());

        ContentResolver cr = context.getContentResolver();
        Uri uri = MovieContract.Movies.CONTENT_URI;
        Uri insertedUri = cr.insert(uri, cv);
        Log.d(TAG, "inserted movie uri:" + insertedUri);
        return insertedUri;
    }

    /**
     * Delete movie by its rowId.
     * @param context the context
     * @param rowId the rowId
     */
    public static void delMovie(Context context, long rowId) {
        if (rowId < 0) throw new SQLException("id is less than 0");
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(MovieContract.Movies.CONTENT_URI, rowId);
        Log.d(TAG, "Delete Uri:" + uri);
        cr.delete(uri, null, null);
    }

    /**
     * Update movie description by its rowId.
     * @param context the context
     * @param rowId the rowId
     * @param description the description
     */
    public static void updateMovieDescrption(Context context, long rowId, String description) {
        if (rowId < 0) throw new SQLException("id is less than 0");
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(MovieContract.Movies.CONTENT_URI, rowId);
        Log.d(TAG, "Updating Uri:" + uri);
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.Movies.DESCRIPTION, description);
        cr.update(uri, cv, null, null);
    }
}
