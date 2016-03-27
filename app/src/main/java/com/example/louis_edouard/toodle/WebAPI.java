package com.example.louis_edouard.toodle;

import com.example.louis_edouard.toodle.moodle.Course;
import com.example.louis_edouard.toodle.moodle.UserProfile;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Louis-Edouard on 3/24/2016.
 */
public class WebAPI {
    public String url;
    private String token;
    public String course;
    public int id;

    public  WebAPI(String token){
        //url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=aa0c0e61965d7e3dc54c6fc8906fe442&wsfunction=core_course_get_courses&moodlewsrestformat=json";
        this.token = token;
        url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken="+ token+ "&wsfunction=core_webservice_get_site_info&moodlewsrestformat=json";
    }

    public UserProfile run() throws IOException {
        //retrieve the content of the html from the url
        OkHttpClient client = new OkHttpClient();
        //request / response
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UserProfile> jsonAdapter = moshi.adapter(UserProfile.class);
        UserProfile userProfile = jsonAdapter.fromJson(json);

        return userProfile;
    }

    public Course runCours()throws IOException{
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Course> jsonAdapter = moshi.adapter(Course.class);
        Course course = jsonAdapter.fromJson(json);

        return  course;
    }

}
