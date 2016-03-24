package com.example.louis_edouard.toodle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Louis-Edouard on 3/24/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "toodle.db";
    static final int DB_VERSION = 1;
    private static SQLiteDatabase db = null;

    static final String TBL_COURS = "cours";
    static final String COURS_ID = "_id";
    static final String COURS_TITLE = "title";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if(db == null) db = getWritableDatabase();
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TBL_COURS + " (";
        query += COURS_ID + " INTEGER PRIMARY KEY, ";
        query += COURS_TITLE + " TEXT)";

        Log.d("SQL", query);
        db.execSQL(query);;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURS);
        onCreate(db);
    }
}
