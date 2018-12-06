package com.example.amusu.teamname_cst2335_final_project.movie;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract between the movie information provider and applications. Contains
 * definitions for the supported URIs and columns.
 * MovieContract defines an extensible database of movie related
 * information.
 * A row in the {@link Movie} table represents a set of data describing a
 * movie information.
 */
@SuppressWarnings("unused")
public final class MovieContract {
    /**
     * This authority is used for writing to or querying from the movie
     * provider. Note: This is set at first run and cannot be changed without
     * breaking apps that access the provider.
     */
    public static final String AUTHORITY = "com.example.amusu.provider";
    /**
     * The content:// style URL for the top-level movie authority
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String TAG = "MovieContract";

    /**
     * This utility class cannot be instantiated
     */
    private MovieContract() {
    }

    /**
     * Columns from the Movie table.
     */
    protected interface MoviesColumns {
        /**
         * The title of the movie. Column name.
         * <P>Type: TEXT</P>
         */
        String TITLE = "title";

        /**
         * The year of the movie. Column name.
         * <P>Type: TEXT</P>
         */
        String YEAR = "year"; // "2007", "2007-"

        /**
         * The rated of the movie. Column name.
         */
        String RATED = "rated"; // MPAA movie ratings "N/A, G, PG, PG-13, R, NC-17"

        /**
         * The released date of the movie. Column name.
         */
        String RELEASED = "released";

        /**
         * The runtime of the movie. Column name.
         */
        String RUNTIME = "runtime";

        /**
         * The genre of the movie. Column name.
         */
        String GENRE = "genre"; //

        /**
         * The director of the movie. Column name.
         */
        String DIRECTOR = "director"; // Michael Bay

        /**
         * The list of writers of the movie. Column name.
         */
        String WRITER = "writer"; //

        /**
         * The list of actors of the movie. Column name.
         */
        String ACTORS = "actors"; //

        /**
         * The plot of the movie. Column name.
         */
        String PLOT = "plot";

        /**
         * The list languages of the movie. Column name.
         */
        String LANGUAGE  = "language";

        /**
         * The country of the movie. Column name.
         */
        String COUNTRY = "country";

        /**
         * The awards of the movie. Column name.
         */
        String AWARDS = "awards";

        /**
         * The poster of the movie. Column name.
         */
        String POSTER = "poster"; // https://m.media-amazon.com/images/kdkk@._V1_SX300.jpg

        /**
         * The poster file of the movie. Column name.
         */
        String POSTER_FILE = "poster_file"; // kdkk@._V1_SX300.jpg

        /**
         * The poster thumbnail of the movie. Column name.
         */
        String POSTER_THUMBNAIL = "poster_thumbnail"; //

        /**
         * The metascore of the movie. Column name.
         */
        String METASCORE = "metascore";

        /**
         * The imdb rating of the movie. Column name.
         */
        String IMDB_RATING = "imdb_rating";

        /**
         * The imdb votes of the movie. Column name.
         */
        String IMDB_VOTES = "imdb_votes";

        /**
         * The imdb id of the movie. Column name.
         */
        String IMDB_ID = "imdb_id"; //tt0418279

        /**
         * The type of the movie. Column name.
         */
        String TYPE = "type"; // movie, series

        /**
         * The description of the movie. Column name.
         */
        String DESCRIPTION = "description";
    }

    /**
     * Constants and helpers for the Movies table, which contains details for
     * individual movie.
     *
     */
    public static final class Movies implements BaseColumns, MoviesColumns {

        /**
         * The content:// style URL for accessing Movies
         */
        @SuppressWarnings("hiding")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movies");
        //uri and MIME type definitions
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.example.amusu.movie";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.example.amusu.movie";
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = TITLE + " COLLATE LOCALIZED ASC";//" DESC";

        /**
         * This utility class cannot be instantiated
         */
        private Movies() {
        }
    }

}
