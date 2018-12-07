package com.example.amusu.teamname_cst2335_final_project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.amusu.teamname_cst2335_final_project.News;

/**
 * this is database operation class
 */

public class DBHelper extends SQLiteOpenHelper {


    private final static String CREATE_TBL = "create table t_news(guid  primary key, title text, link text, pubDate text, author text, category text, description text)";
    private SQLiteDatabase db;

    //A constructor that the SQLiteOpenHelper subclass must have
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        //The constructor of the parent class must be called by super
        super(context, name, factory, version);
    }

    //The constructor of the database, passing three parameters
    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }


    public DBHelper(Context context) {
        this(context, "news.db", null, 1);
    }

    // Callback function, this function will be called the first time it is created, create a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TBL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Insert the method
    public boolean addNews(News news) {
        boolean flag = false;
        //Get the SQLiteDatabase instance
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", news.getTitle());
        values.put("link", news.getLink());
        values.put("guid", news.getGuid());
        values.put("pubDate", news.getPubDate());
        values.put("author", news.getAuthor());
        values.put("category", news.getCategory());
        values.put("description", news.getDescription());
        // this is to insert into the database
        flag = db.insert("t_news", null, values) > 0;
        if (db != null) {
            db.close();
        }
        return flag;
    }

    /**
     * Query all method
     */

    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("t_news", null, null, null, null, null, null, null);
        return c;

    }
}
