package com.example.amusu.teamname_cst2335_final_project.movie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amusu.teamname_cst2335_final_project.MovieInformationActivity;
import com.example.amusu.teamname_cst2335_final_project.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Activity that displays movie detail information.
 * It allows user to enter a description for the movie.
 */
public class MovieDetailActivity extends Activity implements Button.OnClickListener {
    private static final String TAG = "MovieDetailActivity";
    private static final String URL_STRING = "https://www.omdbapi.com/?apikey=f6ae0bf2&r=xml&plot=short&t=";

    private ImageView mPoster;
    private TextView tvTitle, tvYear, tvRuntime, tvActors, tvRating, tvPlot;
    private EditText etDescription;
    private ProgressBar pbProgressBar;
    private long mId;
    private String mTitle;

    /**
     * {@inheritDoc}
     * <p/>Note: After created, immediately start query movie information either by movie title or ID.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        pbProgressBar = findViewById(R.id.movie_ProgressBar);
        pbProgressBar.setVisibility(ProgressBar.VISIBLE);
//        pbProgressBar.setMax(100);
//        pbProgressBar.setProgress(0);

        mPoster = findViewById(R.id.poster);
        tvTitle = findViewById(R.id.title);
        tvTitle.setMovementMethod(new ScrollingMovementMethod());
        tvYear = findViewById(R.id.year);
        tvRuntime = findViewById(R.id.runtime);
        tvActors = findViewById(R.id.actors);
        tvActors.setMovementMethod(new ScrollingMovementMethod());
        tvRating = findViewById(R.id.rating);
        tvPlot = findViewById(R.id.plot);
        tvPlot.setMovementMethod(new ScrollingMovementMethod());
        etDescription = findViewById(R.id.description);
        etDescription.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        mId = intent.getLongExtra("ID", -1);
        mTitle = intent.getStringExtra("TITLE");

        MovieQuery myQuery = new MovieQuery(this);
        if (mId != -1){
            // query database
            myQuery.execute("ID", Long.toString(mId));
        } else {
            // query omdbapi
            myQuery.execute("TITLE", mTitle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     * <p/>Note: when save button is clicked, save description to database.
     *           When delete button is clicked, confirm then delete the movie.
     *
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btSave:
                MovieHelper.updateMovieDescrption(this, mId, etDescription.getText().toString());
                break;
            case R.id.btDelete:
                //MovieHelper.delMovie(this, mId);
                confirmDeleteMovie();
                break;

            case R.id.btGoback:
                Intent intent = new Intent(MovieDetailActivity.this, MovieInformationActivity.class);
                startActivity(intent);
                break;
            default:
                Log.d(TAG, "unknown mId ");
        }
    }

    /**
     * confirm and delete the current movie from database.
     */
    private void confirmDeleteMovie(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        MovieHelper.delMovie(MovieDetailActivity.this, mId);
                        String msg = MovieDetailActivity.this.getString(R.string.movie_deleted, mTitle);
                        Toast toast = Toast.makeText(MovieDetailActivity.this, msg, Toast.LENGTH_SHORT);
                        toast.show();
                        MovieDetailActivity.this.finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msg = getString(R.string.movie_confirm_delete, mTitle);
        builder.setMessage(msg).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    /**
     * Gets bitmap from url
     * @param url the image url
     * @return the bitmap
     */
    static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Gets bitmap from url
     * @param urlString the image url string
     * @return the bitmap
     */
    static Bitmap getImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getImage(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * get file basename without extension
     * @param filename the filename
     * @return the file basename
     */
    static String getFileBaseName (String filename) {
        // Handle null case specially.
        if (filename == null) return null;

        // Get position of last '.'.
        int pos = filename.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return filename;

        // Otherwise return the string, up to the dot.
        return filename.substring(0, pos);
    }

    /**
     * Get file extension from file name
     * @param filename the filename
     * @return the extension
     */
    static String getFileExtension (String filename) {
        // Handle null case specially.
        if (filename == null) return null;

        // Get position of last '.'.
        int pos = filename.lastIndexOf(".");

        // If there wasn't any '.' no extension.
        if (pos == -1) return null;

        // Otherwise return the string after the dot.
        return filename.substring(pos + 1);
    }

    /**
     * check if file exists
     * @param fname the file to check
     * @return true if file exists
     */
    boolean fileExists(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    /**
     * downloads file from url and save to local file
     * @param _url the remote url
     * @param _name the local file
     */
    void downloadFile(String _url, String _name) {
        DataInputStream stream = null;
        FileOutputStream fos = null;

        try {
            URL u = new URL(_url);
            stream = new DataInputStream(u.openStream());
            fos = openFileOutput(_name, Context.MODE_PRIVATE);
            byte data [] = new byte[1024];
            int count;
            while((count = stream.read(data)) != -1){
                fos.write(data, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Generate thumbnail from bitmap and save it to local file
     * @param bitmap the image bitmap
     * @param fname the local file to store thumbnail
     */
    void generateThumbnail(Bitmap bitmap, String fname) {

        FileOutputStream outputStream = null;
        try {
            Bitmap tn = ThumbnailUtils.extractThumbnail(bitmap, 80, 80);
            outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
            tn.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * A async task to query omdbapi with movie title.
     */
    private static class MovieQuery extends AsyncTask<String, Integer, String> {

        private static final int MOVIE_NOT_FOUND = 99;
        private Bitmap poster;
        private String title;
        private String year;
        private String plot;
        private String actors;
        private String imdb_rating;
        private String description;
        private int runtime;
        private final WeakReference<MovieDetailActivity> activityReference;

        // only retain a weak reference to the activity
        MovieQuery(MovieDetailActivity context) {
            activityReference = new WeakReference<>(context);
        }

        /**
         * Query movie from database with row mId
         * @param id the movie row mId
         */
        void queryDb(String id) {
            // get a reference to the activity if it is still there
            MovieDetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            if (isCancelled()) return;
            Movie m = MovieHelper.getMovie(activity, Long.parseLong(id));
            if (isCancelled()) return;
            loadMovie(m);
        }

        /**
         * load movie data
         * @param m the movie object
         */
        void loadMovie(Movie m){
            if (m == null){
                return;
            }
            if (isCancelled()){
                return;
            }
            // get a reference to the activity if it is still there
            MovieDetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            if (isCancelled()) return;
            publishProgress(80);
            title = m.getTitle();
            year = m.getYear();
            actors = m.getActors();
            imdb_rating = m.getRating();
            runtime = m.getRuntime();
            plot = m.getPlot();
            description = m.getDescription();
            try {
                File f = activity.getFileStreamPath(m.getPosterFile());
                poster = BitmapFactory.decodeFile(f.getAbsolutePath(), null);
            }catch(Exception e){
                //Log.w(TAG, "failed to load mPoster", e);
                poster = null;
            }
            publishProgress(90);

        }

        /**
         * Query omdb with movie title
         * @param aTitle the movie title
         */
        void queryOmdb(String aTitle){
            try{
                String query = URLEncoder.encode(aTitle, "UTF-8");
                URL url = new URL(URL_STRING + query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 );
                conn.setConnectTimeout(15000 );
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts query
                conn.connect();

                InputStream stream = conn.getInputStream();

                XmlPullParser xpp = Xml.newPullParser();
                xpp.setInput( stream , null);

                while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                    Log.i(TAG, "In while");
                    if (xpp.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (isCancelled()){
                        return;
                    }
                    if ("error".equals(xpp.getName())){
                        publishProgress(MOVIE_NOT_FOUND); // the error progress value
                        return;
                    }
                    if ("movie".equals(xpp.getName())){
                        publishProgress(25);
                        title = xpp.getAttributeValue(null, "title");
                        title = Html.fromHtml(title).toString();
                        year = xpp.getAttributeValue(null, "year");
                        plot = xpp.getAttributeValue(null, "plot");
                        plot = Html.fromHtml(plot).toString();
                        actors = xpp.getAttributeValue(null, "actors");
                        actors = Html.fromHtml(actors).toString();
                        imdb_rating = xpp.getAttributeValue(null, "imdbRating");
                        String sruntime = xpp.getAttributeValue(null, "runtime");

                        // get a reference to the activity if it is still there
                        MovieDetailActivity activity = activityReference.get();
                        if (activity == null || activity.isFinishing()) return;
                        if (isCancelled()) return;
                        Movie mm = MovieHelper.getMovie(activity, title);
                        if( mm != null){
                            loadMovie(mm);
                            // set mId and mTitle
                            activity.mId = mm.getId();
                            activity.mTitle = mm.getTitle();
                            runtime = mm.getRuntime();
                            return;
                        }
                        String posterUrl = xpp.getAttributeValue(null, "poster");
                        String fn = null;
                        String thumbnail = null;
                        try {
                            URL u = new URL(posterUrl);
                            String f = u.getFile();
                            File ff = new File(f);
                            fn = ff.getName();
                            Log.i(TAG, "file " + fn);

                            publishProgress(50);
                            if (isCancelled()) {
                                return;
                            }
                            if (isCancelled()) {
                                return;
                            }
                            activity.downloadFile(posterUrl, fn);
                            poster = getImage(u);
                            thumbnail = getFileBaseName(fn) + "thumbnail.png";
                            if (isCancelled()) {
                                return;
                            }
                            activity.generateThumbnail(poster, thumbnail);
                            publishProgress(70);
                        }catch (MalformedURLException e){
                            //Log.w(TAG, "failed to get mPoster", e);
                        }

                        Movie m = new Movie();
                        m.setTitle(title);
                        m.setYear(year);
                        m.setPlot(plot);
                        m.setActors(actors);
                        m.setRating(imdb_rating);
                        m.setRuntime(sruntime);
                        runtime = m.getRuntime();
                        m.setPoster(posterUrl);
                        m.setPosterFile(fn);
                        m.setThumbnailFile(thumbnail);
                        if (isCancelled()){
                            return;
                        }
                        Uri uri = MovieHelper.addMovie(activity, m);
                        activity.mId = ContentUris.parseId(uri);
                        activity.mTitle = title;
                        Log.d(TAG, "uri " + uri + ", mId " + activity.mId);
                    }
                }
            }catch(MalformedURLException urlEX){
                urlEX.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }

        /**
         * {@inheritDoc}
         * <p/>Note: arg[0] the query type, support "ID", "TITLE".
         *           arg[1] the query keyword, depends on the type, can be movie mId or movie title.
         *
         */
        @Override
        protected String doInBackground(String... arg) {
            Log.i(TAG, "In doInBackground");
            MovieDetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return "GONE";
            if (isCancelled()) return "CANCEL";
            try {
                if (arg[0].equals("ID")) {
                    //query database with mId;
                    queryDb(arg[1]);
                    return "ID";
                } else {
                    //query omdb with title
                    queryOmdb(arg[1]);
                    return "TITLE";
                }
            }finally {
                publishProgress(100);
            }
        }

        /**
         * {@inheritDoc}
         * <p/>Note: update progress bar in the activity
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // get a reference to the activity if it is still there
            MovieDetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            if (isCancelled()) return;

            activity.pbProgressBar.setProgress(values[0]);
            Log.i(TAG, "In onProgressUpdate");
            if(values[0] == MOVIE_NOT_FOUND) {
                Toast.makeText(activity,
                        activity.getString(R.string.movie_not_found), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        /**
         * {@inheritDoc}
         * <p/>Note: Populate the view with loaded movie data
         */
        @Override
        protected void onPostExecute(String string) {
            if ("GONE".equals(string)){
                return;
            }
            // get a reference to the activity if it is still there
            MovieDetailActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            activity.mPoster.setImageBitmap(poster);
            activity.pbProgressBar.setVisibility(View.GONE);
            activity.tvPlot.setText(plot);
            activity.tvTitle.setText(title);
            activity.tvYear.setText(year);
            activity.tvActors.setText(actors);
            activity.tvRating.setText(imdb_rating);
            String msg = null;
            if (runtime != 0) {
                msg = activity.getString(R.string.runtime_average, runtime);
            } else {
                msg = "N/A";
            }
            activity.tvRuntime.setText(msg);
            activity.etDescription.setText(description, TextView.BufferType.EDITABLE);
        }
    }
}
