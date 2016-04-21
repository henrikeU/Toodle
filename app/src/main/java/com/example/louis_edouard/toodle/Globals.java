package com.example.louis_edouard.toodle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    public final static String KEY_USER_USERNAME = "userName";
    public final static String KEY_USER_PASSWORD = "userPassword";
    public final static String KEY_USER_ID = "userID";
    public final static String KEY_LAST_CONNECTION = "lastConnection";

    public final static int REFRESH_TIME = 1 * 60 * 1000; // fast refresh (30 sec)

    public static boolean IsConnected(Context c){
        ConnectivityManager connMgr = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //boolean isWifiConn = networkInfo.isConnected();
        //networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //boolean isMobileConn = networkInfo.isConnected();
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private static final int MEGABYTE = 1024 * 1024;

    public static String HtmlToText(String html){
        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        return body.text().trim();
    }

    public static String ConvertDate(long unixSeconds){
        Date now  = new Date();
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        long diff = now.getTime() - unixSeconds * 1000L;
        long dayToMil = 24 * 3600 * 1000L;
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

        long now = System.currentTimeMillis();
        long diff = unixSeconds * 1000L - now;
        long minToMillis = 60 * 1000L;
        long hourToMillis = minToMillis * 60;
        long dayToMillis = hourToMillis * 24;

        String time, unit;

        if (diff > dayToMillis) {
            time = String.valueOf(diff / dayToMillis);
            unit = diff > 2 * dayToMillis ? "jours" : "jour";
        }else if (diff > hourToMillis) {
            time = String.valueOf(diff / hourToMillis);
            unit = diff > (2 * hourToMillis) ? "heures" : "heure";
        }else {
            time = String.valueOf(diff / minToMillis);
            unit = diff > 2 * minToMillis ? "minutes" : "minute";
        }

        String formattedTime = "..." + time + " " + unit;
        return formattedTime;
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


