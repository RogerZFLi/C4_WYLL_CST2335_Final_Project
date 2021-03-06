package com.example.amusu.teamname_cst2335_final_project.FoodNutrition;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class for setting up the database for the food activity
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    /**
     * Database variables
     */
    private static final String ACTIVITY_NAME = "FoodDatabaseHelper";
    static String DATABASE_NAME = "Favorites.db";
    static int VERSION_NUM = 1;
    final static String KEY_ID = "_id";
    final static String KEY_NAME = "name";
    final static String KEY_CALORIES = "Calories";
    final static String KEY_FAT = "Fat";
    final static String KEY_CARBS = "Carbs";
    final static String KEY_FIBER = "fiber";
    final static String TABLE_NAME = "Favorites";
    final static String creatTable = "CREATE TABLE " + TABLE_NAME +"("+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+ KEY_NAME +" TEXT NOT NULL, "+ KEY_CALORIES +" TEXT NOT NULL, "+ KEY_FAT +" TEXT NOT NULL, " + KEY_CARBS +  " TEXT NOT NULL, " + KEY_FIBER + " TEXT NOT NULL);";

    /**
     * Constructor: sets up the initial database
     * @param ctx
     */
    public DataBaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    /**
     * On create method creates the table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "OnCreate");
        db.execSQL(creatTable);
    }

    /**
     * On upgrade method, drops and creates new table on upgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("FoodDatabaseHelper", "Calling onUpgrade, oldVersion is" + oldVersion + " newVersion is" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, Old version is " + oldVersion + " New version is " + newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}