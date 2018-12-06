package com.example.amusu.teamname_cst2335_final_project.rogerli.octranspo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLiteOpenHelp to create database or operate a database
 */
public class DBHelper extends SQLiteOpenHelper {
    /**
     * name of the activity
     */
    private final static String ACTIVITY_NAME = "DBHelperr";
    /**
     * name of the created database
     */
    private final static String DATABASE_NAME = "octranspo.db";
    /**
     * name of the station table
     */
    private final static String TABLE_STATION_NAME = "stations";
    /**
     * name of the route table
     */
    private final static String TABLE_ROUTE_NAME = "routes";
    /**
     *name of searched history of  stop numbers
     */
    private final static String TABLE_SEARCH_HISTORY_NAME = "search_history";

    /**
     * attribute name of station number
     */

    private final static String STOP_NO = "station_number";
    /**
     * attribute name of station name
     */
    private final static String STOP_NAME = "station_name";
    private final static String ROUTE_NO = "route_number";

    /**
     * SQLiteDatabase variable
     */
    private SQLiteDatabase db;
    /**
     * the version number of the database
     */
    private final static int VER_NUM = 1;


    /**
     * default constructor
     * @param context the context that the database is running at
     */

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, VER_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_STATION_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STOP_NO + " text, " + STOP_NAME +  " text);");
        db.execSQL("CREATE TABLE " + TABLE_ROUTE_NAME+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROUTE_NO +  " text);");
        db.execSQL("CREATE TABLE " + TABLE_SEARCH_HISTORY_NAME+ " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STOP_NO +  " text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATION_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY_NAME);
        onCreate(db);
    }

    /**
     * to create a writebale database
     * @return
     */

    public SQLiteDatabase openDB(){
        db = getWritableDatabase();
        return db;
    }

    /**
     * select and show all the searched stop numbers
     * @return
     */

    public ArrayList<String> getAllStops(){
        final ArrayList<String> list = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_STATION_NAME, null,null,null,null,null,null,null);
        final int cIdIndex = cursor.getColumnIndex("_id");
        final int cStopNoIndex = cursor.getColumnIndex(STOP_NO);
        final int cStopNameIndex = cursor.getColumnIndex(STOP_NAME);
        for (int i =0; i<cursor.getColumnCount();i++){
            Log.i(ACTIVITY_NAME,"Column name = " + cursor.getColumnName(i));
        }
        while (cursor.moveToNext()) {
            String stopItem = cursor.getString(cStopNoIndex) + " " + cursor.getString(cStopNameIndex);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + stopItem);
            list.add(stopItem);
        }
        return list;
    }

    public ArrayList<String> getAllRoutes() {
        final ArrayList<String> list = new ArrayList<>();
        db = openDB();
        Cursor cursor = db.query(true, TABLE_ROUTE_NAME, null,null,null,null,null,null,null);
        final int cIdIndex = cursor.getColumnIndex("_id");
        final int cRouteNoIndex = cursor.getColumnIndex(ROUTE_NO);
        String routeItem = cursor.getString(cRouteNoIndex);
        list.add("Search History: ");
        for (int i =0; i<cursor.getColumnCount();i++){
            Log.i(ACTIVITY_NAME,"Column name = " + cursor.getColumnName(i));
        }
        while (cursor.moveToNext()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + routeItem);
            list.add(routeItem);
        }
        return list;

    }

    /**
     * add a stop number
     * @param stopNo the input stop number to add
     */
    public void insertToStations(String stopNo) {
        final ContentValues cValue= new ContentValues();
        db = openDB();
        cValue.put(STOP_NO,stopNo);
        cValue.put(STOP_NAME,"");
        long searchId = db.insert(TABLE_STATION_NAME,null,cValue);
        if(searchId>0){
            Log.i(ACTIVITY_NAME,"insert succeed!");

        }else
            Log.i(ACTIVITY_NAME,"insert failed");
    }

    public void insertToRoutes(String routeNo) {
        final ContentValues cValue= new ContentValues();
        db = openDB();
        cValue.put(ROUTE_NO,routeNo);
        long searchId = db.insert(TABLE_ROUTE_NAME,null,cValue);
        if(searchId>0){
            Log.i(ACTIVITY_NAME,"insert succeed!");

        }else
            Log.i(ACTIVITY_NAME,"insert failed");
    }

    public  void deleteRoute(String routeNo) {
        db = openDB();
        db.execSQL("DELETE FROM "+ TABLE_ROUTE_NAME+ " WHERE " + ROUTE_NO +" = " +routeNo);
    }

    /**
     * to delete a stop number
     * @param stopNo the input stop number to delete
     */

    public void deleteStation(String stopNo) {
        db = openDB();
        db.execSQL("DELETE FROM "+ TABLE_STATION_NAME+ " WHERE " + STOP_NO +" = " +stopNo);
    }

    /**
     *
     */

    public void closeDB(){
        if(db!=null)
            db.close();
    }
}
