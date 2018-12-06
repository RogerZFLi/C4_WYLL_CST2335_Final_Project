package com.example.amusu.teamname_cst2335_final_project.movie;

/**
 * Represents a movie information.
 * The information stored includes movie title, year, rating, runtime, main actors, plot, and URL of the movie poster.
 */

public class Movie implements Comparable<Movie> {

    private long mId;       //set movie ID variable
    private String mTitle;  // set movie title variable
    private String mYear;   // set movie year variable
    private String mRating;   // set movie rating
    private int mRuntime;   //set movie runtime
    private String mActors; //set movie actors
    private String mPlot;    //set movie plot variable
    private String mPosterFile; //set movie poster file
    private String mThumbnailFile; //set movie small picture
    private String mPoster;         //set movie poster
    private String mDescription; //set movie description

    /**
     * Get the movie row id.
     * @return the row id
     */
    public long getId() {
        return mId;
    }

    /**
     * Set movie row id.
     * @param mId the row id
     */
    public void setId(long mId) {
        this.mId = mId;
    }

    /**
     * Get the movie title
     * @return the movie title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Set the movie title
     * @param title the movie title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Get the movie year
     * @return the year
     */
    public String getYear() {
        return mYear;
    }

    /**
     * Set the movie released year
     * @param year the released year
     */
    public void setYear(String year) {
        this.mYear = year;
    }

    /**
     * Get movie's imdb rating
     * @return the imdb rating
     */
    public String getRating() {
        return mRating;
    }

    /**
     * Set movie imdb rating
     * @param rating the imdb rating
     */
    public void setRating(String rating) {
        this.mRating = rating;
    }

    /**
     * Get movie runtime in minutes
     * @return the movie runtime
     */
    public int getRuntime() {
        return mRuntime;
    }

    /**
     * Set movie runtime in minutes
     * @param runtime the movie runtime
     */
    public void setRuntime(int runtime) {
        mRuntime = runtime;
    }

    /**
     * Set movie runtime in string format
     * @param runtime the movie runtime string
     */
    public void setRuntime(String runtime) {
        try {
            int sp = runtime.indexOf(" ");
            mRuntime = Integer.parseInt(runtime.substring(0, sp));
        }catch(Exception e){
            mRuntime = 0;
        }
    }

    /**
     * Get main actors
     * @return the main actors
     */
    public String getActors() {
        return mActors;
    }

    /**
     * Set main actors
     * @param actors the main actors
     */
    public void setActors(String actors) {
        this.mActors = actors;
    }

    /**
     * Get the movie plot
     * @return the plot
     */
    public String getPlot() {
        return mPlot;
    }

    /**
     * Set the movie plot
     * @param plot the movie plot
     */
    public void setPlot(String plot) {
        this.mPlot = plot;
    }

    /**
     * Get movie poster url
     * @return the poster url
     */
    public String getPoster() {
        return mPoster;
    }

    /**
     * Set movie poster url
     * @param poster the poster url
     */
    public void setPoster(String poster) {
        this.mPoster = poster;
    }

    /**
     * Get movie local stored poster file
     * @return the local poster file
     */
    public String getPosterFile() {
        return mPosterFile;
    }

    /**
     * Set the poster local cache file name
     * @param posterFile the local poster file name
     */
    public void setPosterFile(String posterFile) {
        this.mPosterFile = posterFile;
    }

    /**
     * Get the thumbnail file name (png format)
     * @return the thumbnail file name
     */
    public String getThumbnailFile() {
        return mThumbnailFile;
    }

    /**
     * Set the thumbnail local stored file name
     * @param thumbnailFile the local stored file name
     */
    public void setThumbnailFile(String thumbnailFile) {
        this.mThumbnailFile = thumbnailFile;
    }

    /**
     * Get movie's description
     * @return the description
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Set movie description
     * @param description the description
     */
    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }

        Movie record = (Movie) o;

        return mId == record.mId;
    }

    @Override
    public int hashCode() {
        return (int) (mId ^ (mId >>> 32));
    }

    /**
     * {@inheritDoc}
     * <p/>Note:utilize sqlite3 database a column declared INTEGER PRIMARY KEY will autoincrement.
     */
    @Override
    public int compareTo(Movie o) {
        long thisId = mId;
        long anotherId = o.mId;
        return (thisId < anotherId ? -1 : (thisId == anotherId ? 0 : 1));
    }

    /**
     * {@inheritDoc}
     * <p/>Note:simple string for debug
     */
    @Override
    public String toString() {
        return "Movie{" +
                "mTitle=" + mTitle +
                ", mYear=" + mYear +
                ", mRating='" + mRating + '\'' +
                ", mRuntime=" + mRuntime +
                '}';
    }
}
