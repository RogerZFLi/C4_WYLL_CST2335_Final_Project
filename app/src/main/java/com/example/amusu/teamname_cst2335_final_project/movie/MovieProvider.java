package com.example.amusu.teamname_cst2335_final_project.movie;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * A movie content provider, which allow users to  movie database.
 */
public class MovieProvider extends ContentProvider {
    private static final String UNKNOWN_URI = "Unknown URI ";
    private static final String TAG = "MovieProvider";
    private static final String TBL_MOVIES = "movies";
    private static final String DBNAME = "movie.db";
    //Setup projection Map
    //Projection maps are similar to "as" (column alias) construct
    //in an sql statement where by you can rename the columns.
    private static final Map<String, String> sUsersProjectionMap;
    //Provide a mechanism to identify all the incoming uri patterns.
    private static final UriMatcher sUriMatcher;
    private static final int MATCH_MOVIE_COLLECTION = 1;
    private static final int MATCH_MOVIE_FILTER = 2;
    private static final int MATCH_MOVIE_ITEM = 3;
    // A string that defines the SQL statement for creating a table
    private static final String SQL_CREATE_MOVIES = "CREATE TABLE IF NOT EXISTS" +
            " movies " +                       // Table's name
            " ( " +                           // The columns in the table
            " _id INTEGER PRIMARY KEY, " +
            " title TEXT UNIQUE, " +
            " year TEXT, " +
            " rated TEXT, " +
            " released TEXT, " +
            " runtime INTEGER, " +
            " genre TEXT, " +
            " director TEXT, " +
            " writer TEXT, " +
            " actors TEXT, " +
            " plot TEXT, " +
            " language TEXT, " +
            " country TEXT, " +
            " awards TEXT, " +
            " poster TEXT, " +
            " poster_file TEXT, " +
            " poster_thumbnail TEXT, " +
            " metascore TEXT, " +
            " imdb_rating TEXT, " +
            " imdb_votes TEXT, " +
            " imdb_id TEXT, " +
            " type TEXT, " +
            " description TEXT " +
            " ) ";


    static {
        sUsersProjectionMap = new HashMap<>();
        sUsersProjectionMap.put(MovieContract.Movies._ID, MovieContract.Movies._ID);
        sUsersProjectionMap.put(MovieContract.Movies.TITLE, MovieContract.Movies.TITLE);
        sUsersProjectionMap.put(MovieContract.Movies.YEAR, MovieContract.Movies.YEAR);
        sUsersProjectionMap.put(MovieContract.Movies.RATED, MovieContract.Movies.RATED);
        sUsersProjectionMap.put(MovieContract.Movies.RELEASED, MovieContract.Movies.RELEASED);
        sUsersProjectionMap.put(MovieContract.Movies.RUNTIME, MovieContract.Movies.RUNTIME);
        sUsersProjectionMap.put(MovieContract.Movies.GENRE, MovieContract.Movies.GENRE);
        sUsersProjectionMap.put(MovieContract.Movies.DIRECTOR, MovieContract.Movies.DIRECTOR);
        sUsersProjectionMap.put(MovieContract.Movies.WRITER, MovieContract.Movies.WRITER);
        sUsersProjectionMap.put(MovieContract.Movies.ACTORS, MovieContract.Movies.ACTORS);
        sUsersProjectionMap.put(MovieContract.Movies.PLOT, MovieContract.Movies.PLOT);
        sUsersProjectionMap.put(MovieContract.Movies.LANGUAGE, MovieContract.Movies.LANGUAGE);
        sUsersProjectionMap.put(MovieContract.Movies.COUNTRY, MovieContract.Movies.COUNTRY);
        sUsersProjectionMap.put(MovieContract.Movies.AWARDS, MovieContract.Movies.AWARDS);
        sUsersProjectionMap.put(MovieContract.Movies.POSTER, MovieContract.Movies.POSTER);
        sUsersProjectionMap.put(MovieContract.Movies.POSTER_FILE, MovieContract.Movies.POSTER_FILE);
        sUsersProjectionMap.put(MovieContract.Movies.POSTER_THUMBNAIL, MovieContract.Movies.POSTER_THUMBNAIL);
        sUsersProjectionMap.put(MovieContract.Movies.METASCORE, MovieContract.Movies.METASCORE);
        sUsersProjectionMap.put(MovieContract.Movies.IMDB_RATING, MovieContract.Movies.IMDB_RATING);
        sUsersProjectionMap.put(MovieContract.Movies.IMDB_VOTES, MovieContract.Movies.IMDB_VOTES);
        sUsersProjectionMap.put(MovieContract.Movies.IMDB_ID, MovieContract.Movies.IMDB_ID);
        sUsersProjectionMap.put(MovieContract.Movies.TYPE, MovieContract.Movies.TYPE);
        sUsersProjectionMap.put(MovieContract.Movies.DESCRIPTION, MovieContract.Movies.DESCRIPTION);
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MovieContract.AUTHORITY, "movies", MATCH_MOVIE_COLLECTION);
        sUriMatcher.addURI(MovieContract.AUTHORITY, "movies/filter/*", MATCH_MOVIE_FILTER);
        sUriMatcher.addURI(MovieContract.AUTHORITY, "movies/#", MATCH_MOVIE_ITEM);
    }

    private MainDatabaseHelper mOpenHelper;

    /**
     * Appends id from uri to where clause.
     * @param uri       the uri containing id
     * @param selection the selection string
     * @return the new selection string containing id filtering
     */
    private String appendIdSelection(final Uri uri, final String selection) {
        //TODO check if _id provided
        String rowId = uri.getPathSegments().get(1);
        return BaseColumns._ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String idSelection;
        switch (sUriMatcher.match(uri)) {
            case MATCH_MOVIE_COLLECTION:
                count = db.delete(TBL_MOVIES, selection, selectionArgs);
                break;
            case MATCH_MOVIE_ITEM:
                idSelection = appendIdSelection(uri, selection);
                count = db.delete(TBL_MOVIES, idSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        ContentResolver cr = getContext().getContentResolver();
        if(cr != null){
            cr.notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_MOVIE_COLLECTION:
                return MovieContract.Movies.CONTENT_TYPE;
            case MATCH_MOVIE_FILTER:
                return MovieContract.Movies.CONTENT_TYPE;
            case MATCH_MOVIE_ITEM:
                return MovieContract.Movies.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }
    }

    /**
     * Check values contains mandatory columns for movie insert.
     *
     * @param uri    the uri
     * @param values the user row values
     */
    private void checkMovie(final Uri uri, final ContentValues values) {
        if (!values.containsKey(MovieContract.Movies.TITLE)) {
            throw new SQLException("Failed to insert Movie row because Movie title is needed " + uri);
        }
        if (!values.containsKey(MovieContract.Movies.YEAR)) {
            throw new SQLException("Failed to insert Movie row because Movie year is needed " + uri);
        }
        if (!values.containsKey(MovieContract.Movies.PLOT)) {
            throw new SQLException("Failed to insert Movie row because Movie plot is needed " + uri);
        }
        if (!values.containsKey(MovieContract.Movies.ACTORS)) {
            throw new SQLException("Failed to insert Movie row because Movie actors is needed " + uri);
        }
    }

    @Override
    public Uri insert(final Uri uri,  ContentValues initialValues) {
        String table;
        Uri contentUri;
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        switch (sUriMatcher.match(uri)) {
            case MATCH_MOVIE_COLLECTION:
                checkMovie(uri, values);
                table = TBL_MOVIES;
                contentUri = MovieContract.Movies.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(table, null, values);
        if (rowId > 0) {
            Uri insertedUri = ContentUris.withAppendedId(contentUri, rowId);
            ContentResolver cr = getContext().getContentResolver();
            if(cr != null) {
                cr.notifyChange(insertedUri, null);
            }
            return insertedUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    /**
     * Get the internal database.
     *
     * @return the sqlite database object.
     */
    public SQLiteDatabase getDatabase() {
        return mOpenHelper.getWritableDatabase();
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "main onCreate called");
        /*
         * Creates a new helper object. This method always returns quickly.
         * Notice that the database itself isn't created or opened
         * until SQLiteOpenHelper.getWritableDatabase is called
         */
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String defaultSort;
        switch (sUriMatcher.match(uri)) {
            case MATCH_MOVIE_COLLECTION:
                qb.setTables(TBL_MOVIES);
                qb.setProjectionMap(sUsersProjectionMap);
                defaultSort = MovieContract.Movies.DEFAULT_SORT_ORDER;
                break;

            case MATCH_MOVIE_FILTER:
                qb.setTables(TBL_MOVIES);
                qb.setProjectionMap(sUsersProjectionMap);
                qb.appendWhere(MovieContract.Movies.TITLE + " like '%" + uri.getLastPathSegment() + "%'");
                defaultSort = MovieContract.Movies.DEFAULT_SORT_ORDER;
                break;

            case MATCH_MOVIE_ITEM:
                qb.setTables(TBL_MOVIES);
                qb.setProjectionMap(sUsersProjectionMap);
                qb.appendWhere(MovieContract.Movies._ID + "=" + uri.getPathSegments().get(1));
                defaultSort = MovieContract.Movies.DEFAULT_SORT_ORDER;
                break;

            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = defaultSort;
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);


        // Tell the cursor what uri to watch,
        // so it knows when its source data changes
        ContentResolver cr = getContext().getContentResolver();
        if(cr != null) {
            c.setNotificationUri(cr, uri);
        }
        return c;
    }

    @Override
    public int update(Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String idSelection;
        switch (sUriMatcher.match(uri)) {
            case MATCH_MOVIE_COLLECTION:
                count = db.update(TBL_MOVIES, values, selection, selectionArgs);
                break;

            case MATCH_MOVIE_ITEM:
                idSelection = appendIdSelection(uri, selection);
                count = db.update(TBL_MOVIES, values, idSelection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        if (count > 0) {
            ContentResolver cr = getContext().getContentResolver();
            if(cr != null) {
                cr.notifyChange(uri, null);
            }
        }
        return count;
    }

    /**
     * Helper class that actually creates and manages the provider's underlying data repository.
     */
    private static final class MainDatabaseHelper extends SQLiteOpenHelper {

        /*
         * Instantiates an open helper for the provider's SQLite data repository
         * Do not do database creation and upgrade here.
         *
         * version 1: the base version
         */
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
            Log.d(TAG, "create a new MainDatabaseHelp instance");
        }

        /*
         * Creates the data repository. This is called when the provider attempts to open the
         * repository and SQLite reports that it doesn't exist.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "create database " + DBNAME);
            // Creates the main table
            db.execSQL(SQL_CREATE_MOVIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "upgrade database from " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS movies");
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "downgrade database from " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS movies");
            onCreate(db);
        }
    }

}
