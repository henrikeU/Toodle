package com.example.louis_edouard.toodle.moodle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Louis-Edouard on 3/25/2016.
 */
public class Globals {
    public final static String SHARED_PREFERENCES_NAME = "sharedPrefs";
    public final static String KEY_USER_TOKEN = "userToken";
    public final static String KEY_USER_ID = "userID";

    public static String ConvertDate(long unixSeconds){
        Date now  = new Date();
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        long diff = now.getTime() - unixSeconds * 1000L;
        long dayToMil = 24*3600*1000L;
        SimpleDateFormat sdf;
        if (diff - dayToMil < 0)
            sdf = new SimpleDateFormat("HH:mm"); // the format of your date
        else if (diff - 2 * dayToMil < 0)
            return "Hier";
        else
            sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}


