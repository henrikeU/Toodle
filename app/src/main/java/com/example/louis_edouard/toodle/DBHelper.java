package com.example.louis_edouard.toodle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.louis_edouard.toodle.moodle.Course;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;

import java.util.List;

/**
 * Created by Louis-Edouard on 3/24/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "toodle.db";
    static final int DB_VERSION = 4;
    private static SQLiteDatabase db = null;

    static final String TBL_COURSE = "enrolledcourse";
    static final String COURSE_ID = "_id";
    static final String COURSE_SHORTNAME = "shortname";
    static final String COURSE_FULLNAME = "fullname";
    static final String COURSE_IDNUMBER = "idnumber";
    static final String COURSE_SUMMARY = "summary";
    static final String COURSE_STATUS = "status";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if(db == null) db = getWritableDatabase();
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TBL_COURSE + " (";
        query += COURSE_ID + " INTEGER PRIMARY KEY, ";
        query += COURSE_SHORTNAME + " TEXT, ";
        query += COURSE_FULLNAME + " TEXT, ";
        query += COURSE_IDNUMBER + " TEXT, ";
        query += COURSE_SUMMARY + " TEXT, ";
        query += COURSE_STATUS + " INTEGER DEFAULT 0);";

        Log.d("SQL", query);
        db.execSQL(query);;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSE);
        onCreate(db);
    }

    public int addCourses(List<EnrolledCourse> enrolledCourses){
        int nb = 0;
        EnrolledCourse enrolledCourse;
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < enrolledCourses.size(); i++){
            enrolledCourse = enrolledCourses.get(i);
            contentValues.clear();
            contentValues.put(COURSE_ID, enrolledCourse.id);
            contentValues.put(COURSE_SHORTNAME, enrolledCourse.shortname);
            contentValues.put(COURSE_FULLNAME, enrolledCourse.fullname);
            contentValues.put(COURSE_IDNUMBER, enrolledCourse.idnumber);
            contentValues.put(COURSE_SUMMARY, enrolledCourse.summary);
            try {
                db.insertOrThrow(TBL_COURSE, null, contentValues);
                nb++;
            }catch (SQLException e) {};
        }
        return nb;
    }

    public void deleteCourse(long id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_STATUS, 2);
        String whereClause = COURSE_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        db.update(TBL_COURSE, contentValues, whereClause, whereArgs);
    }

    public Cursor listeCourse(){
        Cursor c;
        c = db.rawQuery("SELECT * FROM " + TBL_COURSE + " WHERE " + COURSE_STATUS + " = 0 ORDER BY " + COURSE_ID + " DESC", null);
        return c;
    }
}
