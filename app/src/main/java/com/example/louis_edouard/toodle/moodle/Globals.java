package com.example.louis_edouard.toodle.moodle;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Louis-Edouard on 3/25/2016.
 */
public class Globals {
    public final static String SHARED_PREFERENCES_NAME = "sharedPrefs";
    public final static String KEY_USER_TOKEN = "userToken";
    public final static String KEY_USER_ID = "userID";

    private static final int MEGABYTE = 1024 * 1024;

    public static String ConvertDate(long unixSeconds){
        Date now  = new Date();
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        long diff = now.getTime() - unixSeconds * 1000L;
        long dayToMil = 24*3600*1000L;
        SimpleDateFormat sdf;
        // Messagerie only
        if (diff - dayToMil < 0)
            sdf = new SimpleDateFormat("HH:mm"); // the format of your date
        else if (diff - 2 * dayToMil < 0)
            return "Hier";
        else
            sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formatting (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String EventConvertDate(long unixSeconds){
        Date now  = new Date();
        long diff = now.getTime() - unixSeconds*1000L;
        Date date = new Date(diff); // *1000 is to convert seconds to milliseconds

        long dayToSec = 24*3600;
        long hourToSec= 3600;
        long minToSec= 60;
        SimpleDateFormat sdf;
        String unit;
        if (unixSeconds > dayToSec) {
            sdf = new SimpleDateFormat("dd");
            unit = unixSeconds > 2 * dayToSec ? "days" : "day";
        }else if (unixSeconds > hourToSec) {
            sdf = new SimpleDateFormat("HH");
            unit = unixSeconds > 2 * hourToSec ? "hrs" : "hr";
        }else {
            sdf = new SimpleDateFormat("mm");
            unit = unixSeconds > 2 * minToSec ? "mins" : "min";
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        Log.d("time_test", formattedDate+" "+unit);
        return formattedDate;
    }

    public static void DownloadFile(String fileURL, File directory){
        try{
            URL url = new URL(fileURL);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0)
                fileOutputStream.write(buffer, 0, bufferLength);

            fileOutputStream.close();

        }
        catch (FileNotFoundException e){}
        catch (MalformedURLException e){}
        catch (IOException e){}
    }
}


