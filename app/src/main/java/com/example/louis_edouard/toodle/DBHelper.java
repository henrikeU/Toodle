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
    static final int DB_VERSION = 5;
    private static SQLiteDatabase db = null;

    // Common Column
    static final String KEY_ID = "_id";

    // ENROLLEDCOURSE Table
    static final String TBL_ENROLLEDCOURSE = "enrolledCourse";
    static final String COURSE_SHORTNAME = "shortName";
    static final String COURSE_FULLNAME = "fullName";
    static final String COURSE_IDNUMBER = "idNumber";
    static final String COURSE_SUMMARY = "summary";
    static final String COURSE_STATUS = "status";

    // COURSECONTENT Table
    static final String TBL_COURSECONTENT = "courseContent";
    static final String COURSECONTENT_NAME = "name";
    static final String COURSECONTENT_SUMMARY = "summary";
    static final String KEY_ENROLLEDCOURSE_ID = TBL_ENROLLEDCOURSE + KEY_ID;

    // COURSEMODULE Table
    static final String TBL_COURSEMODULE = "courseModule";
    static final String COURSEMODULE_NAME = "name";

    // Table CREATE Statements
    // CourseContent table create statement
    static final String CREATE_TABLE_ENROLLEDCOURSE = "CREATE TABLE " + TBL_ENROLLEDCOURSE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COURSE_SHORTNAME + " TEXT, " +
            COURSE_FULLNAME + " TEXT, " +
            COURSE_IDNUMBER + " TEXT, " +
            COURSE_SUMMARY + " TEXT, " +
            COURSE_STATUS + " INTEGER DEFAULT 0)";
    // CourseContent table create statement
    static final String CREATE_TABLE_COURSECONTENT = "CREATE TABLE " + TBL_COURSECONTENT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COURSECONTENT_NAME + " TEXT, " +
            COURSECONTENT_SUMMARY + " TEXT, " +
            KEY_ENROLLEDCOURSE_ID + " INTEGER, " +
            "FOREIGN KEY(" + KEY_ENROLLEDCOURSE_ID +") REFERENCES " + TBL_ENROLLEDCOURSE + "(" + KEY_ID + "))";

    // CourseModule table create statement
    static final String CREATE_TABLE_COURSEMODULE = "CREATE TABLE " + TBL_COURSEMODULE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COURSEMODULE_NAME + " TEXT)";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if(db == null) db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d("SQL", query);
        db.execSQL(CREATE_TABLE_ENROLLEDCOURSE);
        db.execSQL(CREATE_TABLE_COURSECONTENT);
        db.execSQL(CREATE_TABLE_COURSEMODULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ENROLLEDCOURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSECONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSEMODULE);
        onCreate(db);
    }

    public int addCourses(List<EnrolledCourse> enrolledCourses){
        int nb = 0;
        EnrolledCourse enrolledCourse;
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < enrolledCourses.size(); i++){
            enrolledCourse = enrolledCourses.get(i);
            contentValues.clear();
            contentValues.put(KEY_ID, enrolledCourse.id);
            contentValues.put(COURSE_SHORTNAME, enrolledCourse.shortname);
            contentValues.put(COURSE_FULLNAME, enrolledCourse.fullname);
            contentValues.put(COURSE_IDNUMBER, enrolledCourse.idnumber);
            contentValues.put(COURSE_SUMMARY, enrolledCourse.summary);
            try {
                db.insertOrThrow(TBL_ENROLLEDCOURSE, null, contentValues);
                nb++;
            }catch (SQLException e) {};
        }
        return nb;
    }

    public void deleteCourse(long id) { updateCourseStatus(id, 2); }

    public void archiveCourse(long id){ updateCourseStatus(id, 1); }

    private void updateCourseStatus(long id, int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_STATUS, status);
        String whereClause = KEY_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        db.update(TBL_ENROLLEDCOURSE, contentValues, whereClause, whereArgs);
    }

    public Cursor getCourse(long id) {
        Cursor c;
        c = db.query(TBL_ENROLLEDCOURSE, new String[]{KEY_ID, COURSE_SHORTNAME, COURSE_FULLNAME, COURSE_IDNUMBER }, KEY_ID + "= ?", new String[] { String.valueOf(id) }, null, null, null);
        return c;
    }

    public Cursor getAllCourses(){
        Cursor c;
        c = db.rawQuery("SELECT * FROM " + TBL_ENROLLEDCOURSE + " WHERE " + COURSE_STATUS + " != 2 ORDER BY " + KEY_ID + " DESC", null);
        return c;
    }
}
