package com.example.louis_edouard.toodle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CalendarEvent;
import com.example.louis_edouard.toodle.moodle.Course;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.UserProfileSearch;

import java.util.List;

/**
 * Created by Louis-Edouard on 3/24/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "toodle.db";
    static final int DB_VERSION = 8;
    private static SQLiteDatabase db = null;

    // Common Column
    static final String KEY_ID = "_id";

    // ENROLLEDCOURSE Table
    static final String TBL_COURSE = "course";
    static final String COURSE_SHORTNAME = "shortName";
    static final String COURSE_FULLNAME = "fullName";
    static final String COURSE_IDNUMBER = "idNumber";
    static final String COURSE_SUMMARY = "summary";
    static final String COURSE_ISENROLLED = "isEnrolled";
    static final String COURSE_STATUS = "status";

    // COURSECONTENT Table
    static final String TBL_COURSECONTENT = "courseContent";
    static final String COURSECONTENT_NAME = "name";
    static final String COURSECONTENT_SUMMARY = "summary";
    static final String KEY_ENROLLEDCOURSE_ID = TBL_COURSE + KEY_ID;

    // COURSEMODULE Table
    static final String TBL_COURSEMODULE = "courseModule";
    static final String COURSEMODULE_NAME = "name";

    // EVENT Table
    static final String TBL_EVENT = "event";
    static final String EVENT_NAME = "name";
    static final String EVENT_DESCRIPTION = "description";
    static final String EVENT_TYPE = "type";
    static final String EVENT_TIMESTART = "timeStart";
    static final String EVENT_TIMEDURATION = "timeDuration";
    // dynamic fields
    static final String EVENT_FORMATDATE = "eventDate";
    static final String EVENT_FORMATTIMESTART = "eventTimeStart";
    static final String EVENT_FORMATTIMEEND = "eventTimeEnd";

    // CONTACT Table
    static final String TBL_CONTACT = "contact";
    static final String CONTACT_FULLNAME = "fullName";
    static final String CONTACT_EMAIL = "email";
    static final String CONTACT_PHONE = "phone";
    static final String CONTACT_CELL = "cell";
    static final String CONTACT_ADDRESS = "address";
    static final String CONTACT_CITY = "city";
    static final String CONTACT_COUNTRY = "country";
    static final String CONTACT_ISFAVORITE = "isFavorite";

    // Table CREATE Statements
    // CourseContent table create statement
    static final String CREATE_TABLE_COURSE = "CREATE TABLE " + TBL_COURSE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COURSE_SHORTNAME + " TEXT, " +
            COURSE_FULLNAME + " TEXT, " +
            COURSE_IDNUMBER + " TEXT, " +
            COURSE_SUMMARY + " TEXT, " +
            COURSE_ISENROLLED + " BOOLEAN DEFAULT 1, " +
            COURSE_STATUS + " TINYINT DEFAULT 0)";

    // CourseContent table create statement
    static final String CREATE_TABLE_COURSECONTENT = "CREATE TABLE " + TBL_COURSECONTENT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COURSECONTENT_NAME + " TEXT, " +
            COURSECONTENT_SUMMARY + " TEXT, " +
            KEY_ENROLLEDCOURSE_ID + " INTEGER, " +
            "FOREIGN KEY(" + KEY_ENROLLEDCOURSE_ID +") REFERENCES " + TBL_COURSE + "(" + KEY_ID + "))";

    // CourseModule table create statement
    static final String CREATE_TABLE_COURSEMODULE = "CREATE TABLE " + TBL_COURSEMODULE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COURSEMODULE_NAME + " TEXT)";

    // Event table create statement
    static final String CREATE_TABLE_EVENT = "CREATE TABLE " + TBL_EVENT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            EVENT_NAME + " TEXT, " +
            EVENT_DESCRIPTION + " TEXT, " +
            EVENT_TYPE + " TEXT, " +
            EVENT_TIMESTART + " INTEGER, " +
            EVENT_TIMEDURATION + " INTEGER DEFAULT 0)";

    // Contact table create statement
    static final String CREATE_TABLE_CONTACT = "CREATE TABLE " + TBL_CONTACT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            CONTACT_FULLNAME + " TEXT, " +
            CONTACT_EMAIL + " TEXT, " +
            CONTACT_PHONE + " TEXT, " +
            CONTACT_CELL + " TEXT, " +
            CONTACT_ADDRESS + " TEXT, " +
            CONTACT_CITY + " TEXT, " +
            CONTACT_COUNTRY + " TEXT, " +
            CONTACT_ISFAVORITE + " BOOLEAN DEFAULT 0)";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if(db == null) db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d("SQL", query);
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_COURSECONTENT);
        db.execSQL(CREATE_TABLE_COURSEMODULE);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSECONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSEMODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CONTACT);
        onCreate(db);
    }

    private Cursor getById(String table, String[] columns, long id){
        String whereClause = KEY_ID + "= ?";
        String[] whereArgs = new String[]{ String.valueOf(id) };
        Cursor c = db.query(table, columns, whereClause, whereArgs, null, null, null);

        return c;
    }

    private Cursor getById(String table, long id, String orderColumn){
        String query = "SELECT * FROM " + table + " WHERE " + KEY_ID + " = " + id;
        if(orderColumn != null)
            query += " ORDER BY " + orderColumn;

        Cursor c = db.rawQuery(query, null);

        return c;
    }

    private Cursor getAll(String table, String orderColumn) {
        String query = "SELECT * FROM " + table;
        if(orderColumn != null)
            query += " ORDER BY " + orderColumn;

        Cursor c = db.rawQuery(query, null);

        return c;
    }

    /**
     * TABLE Course Methods
     * addCourses: add a list of course to the table
     * deleteCourse: update the status of a course to 2 => DELETE
     * archiveCourse: update the status of a course to 1 => ARCHIVE
     * getCourse: return a course
     * getAllCourses: return all courses where status is not 2 => DELETE
     */

    public int addCourses(List<EnrolledCourse> Courses){
        int nb = 0;
        ContentValues contentValues = new ContentValues();

        for(EnrolledCourse course : Courses){
            contentValues.clear();
            contentValues.put(KEY_ID, course.id);
            contentValues.put(COURSE_SHORTNAME, course.shortname);
            contentValues.put(COURSE_FULLNAME, course.fullname);
            contentValues.put(COURSE_IDNUMBER, course.idnumber);
            contentValues.put(COURSE_SUMMARY, course.summary);
            try {
                db.insertOrThrow(TBL_COURSE, null, contentValues);
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
        db.update(TBL_COURSE, contentValues, whereClause, whereArgs);
    }

    public Cursor getCourse(long id) {
        String[] columns = new String[]{ KEY_ID, COURSE_SHORTNAME, COURSE_FULLNAME, COURSE_IDNUMBER };
        return getById(TBL_COURSE, columns, id);
    }

    public Cursor getAllCourses(){
        Cursor c;
        c = db.rawQuery("SELECT * FROM " + TBL_COURSE + " WHERE " + COURSE_STATUS + " != 2 ORDER BY " + KEY_ID + " DESC", null);
        return c;
    }

    /**
     * TABLE Event Methods
     * addEvents: add a list of events to the table
     * getAllEvents: return all events
     * getAllFutureEvents: return all events that start after the current time
     */

    public int addEvents(List<CalendarEvent> events){
        int nb = 0;
        ContentValues contentValues = new ContentValues();
        //db.execSQL("DELETE FROM " + TBL_EVENT);
        for(CalendarEvent event : events) {
            contentValues.clear();
            contentValues.put(KEY_ID, event.id);
            contentValues.put(EVENT_NAME, event.name);
            contentValues.put(EVENT_DESCRIPTION, event.description);
            contentValues.put(EVENT_TYPE, event.eventtype);
            contentValues.put(EVENT_TIMESTART, event.timestart);
            contentValues.put(EVENT_TIMEDURATION, event.timeduration);
            try {
                db.insertWithOnConflict(TBL_EVENT, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                nb++;
            }catch (SQLException e) {};
        }
        return nb;
    }

    public Cursor getAllEvents(){

        return getAll(TBL_EVENT, null);
    }

    public Cursor getAllFutureEvents() {
        Cursor c;

        String[] columns = new String[]{ KEY_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_TYPE, EVENT_TIMESTART,
                                        "date(" + EVENT_TIMESTART + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATDATE,
                                        "strftime('%H:%M', " + EVENT_TIMESTART + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATTIMESTART,
                                        "strftime('%H:%M', " + EVENT_TIMESTART + " + " + EVENT_TIMEDURATION + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATTIMEEND};
        String whereClause = EVENT_TIMESTART + " > strftime('%s', 'now')";
        String[] whereArgs = new String[] { EVENT_TIMESTART };
        String orderBy = EVENT_TIMESTART;
        String limit = "10";
        c = db.query(TBL_EVENT, columns, whereClause, null, null, null, orderBy, limit);

        c.moveToFirst();

        return c;
    }

    public Cursor getEventsByMonth(String mDate) {
        Cursor c;

        String[] columns = new String[]{ KEY_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_TYPE, EVENT_TIMESTART,
                "date(" + EVENT_TIMESTART + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATDATE,
                "strftime('%H:%M', " + EVENT_TIMESTART + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATTIMESTART,
                "strftime('%H:%M', " + EVENT_TIMESTART + " + " + EVENT_TIMEDURATION + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATTIMEEND};
        String whereClause = "date(" + EVENT_TIMESTART + ", 'unixepoch', 'localtime') = date('" + mDate + "')";
        String orderBy = EVENT_TIMESTART;
        c = db.query(TBL_EVENT, columns, whereClause, null, null, null, orderBy, null);
        c.moveToFirst();

        return c;
    }

    public Cursor getEventsByMonth() {
        Cursor c;

       // c = db.rawQuery("SELECT date(timeStart, 'unixepoch', 'localtime') testing, _id FROM " + TBL_EVENT + " GROUP BY testing", null);
        String[] columns = new String[]{ KEY_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_TYPE, EVENT_TIMESTART,
                "date(" + EVENT_TIMESTART + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATDATE,
                "strftime('%H:%M', " + EVENT_TIMESTART + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATTIMESTART,
                "strftime('%H:%M', " + EVENT_TIMESTART + " + " + EVENT_TIMEDURATION + ", 'unixepoch', 'localtime') AS " + EVENT_FORMATTIMEEND};
        String groupBy = EVENT_FORMATDATE;
        String orderBy = EVENT_TIMESTART;
        c = db.query(TBL_EVENT, columns, null, null, groupBy, null, orderBy, null);
        c.moveToFirst();

        return c;
    }

    /**
     * TABLE Event Methods
     * addContacts: add a list of contact to the table
     * getAllContacts: return all contact
     */

    public int addContacts(List<UserProfileSearch> users){
        int nb = 0;
        ContentValues contentValues = new ContentValues();

        for(UserProfileSearch user : users){
            contentValues.clear();
            contentValues.put(KEY_ID, user.id);
            contentValues.put(CONTACT_FULLNAME, user.fullname);
            contentValues.put(CONTACT_EMAIL, user.email);
            contentValues.put(CONTACT_PHONE, user.phone1);
            contentValues.put(CONTACT_CELL, user.phone2);
            contentValues.put(CONTACT_ADDRESS, user.address);
            contentValues.put(CONTACT_CITY, user.city);
            contentValues.put(CONTACT_COUNTRY, user.country);
            try {
                db.insertWithOnConflict(TBL_CONTACT, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                nb++;
            }catch (SQLException e) {};
        }
        return nb;
    }

    public Cursor getContact(long id) {
        String[] columns = new String[]{ KEY_ID, CONTACT_FULLNAME, CONTACT_ADDRESS, CONTACT_EMAIL, CONTACT_PHONE, CONTACT_CELL };
        return getById(TBL_CONTACT, columns, id);
    }

    public Cursor filterContact(CharSequence str) {
        Cursor c;
        String[] columns = new String[]{ KEY_ID, CONTACT_FULLNAME, CONTACT_ADDRESS, CONTACT_EMAIL, CONTACT_PHONE, CONTACT_CELL };
        String whereClause = CONTACT_FULLNAME + " LIKE ? ";
        String[]  whereArgs = { "%" + str + "%"};
        c = db.query(TBL_CONTACT, columns, whereClause, whereArgs, null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getAllContacts(){
        return getAll(TBL_CONTACT, CONTACT_FULLNAME);
    }

}
