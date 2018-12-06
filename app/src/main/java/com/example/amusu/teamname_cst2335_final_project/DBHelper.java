package com.example.amusu.teamname_cst2335_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "news.db";
    private final static String TABLE_NAME = "t_news";


    private final static String CREATE_TBL = "create table t_news(guid  primary key, title text, link text, pubDate text, author text, category text, description text)";
    private SQLiteDatabase db;

    //create a constructor for SQLiteOpenHelper
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        //must call the constructor of the parent class through super
        super(context, name, factory, version);
    }

    //create a constructor for database with 3 parameters
    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }


    public DBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    // create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TBL);
    }

    //Callback，This function is called when you construct the passed version of DBHelper with the previous Version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //insert the boolean method
    public boolean addNews(News news) {
        boolean flag = false;
        //get the SQLiteDatabase instance
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", news.getTitle());
        values.put("link", news.getLink());
        values.put("guid", news.getGuid());
        values.put("pubDate", news.getPubDate());
        values.put("author", news.getAuthor());
        values.put("category", news.getCategory());
        values.put("description", news.getDescription());
        //insert data to database
        flag = db.insert(TABLE_NAME, null, values) > 0;
        db.close();
        return flag;
    }

    //read from database
    public Cursor query() {
        SQLiteDatabase db = getReadableDatabase();
        // retrieve Cursor
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return c;

    }

    //close database
    public void close() {
        if (db != null) {
            db.close();
        }
    }

}
