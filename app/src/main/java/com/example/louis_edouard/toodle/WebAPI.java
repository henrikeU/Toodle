package com.example.louis_edouard.toodle;

import android.util.Log;
import android.webkit.WebView;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.Token;
import com.example.louis_edouard.toodle.moodle.UserProfile;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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

    public WebAPI(){}

    public  WebAPI(String token){
        //url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=aa0c0e61965d7e3dc54c6fc8906fe442&wsfunction=core_course_get_courses&moodlewsrestformat=json";
        this.token = token;
        url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=" + token;
    }

    public String baseCode() throws IOException{
        //retrieve the content of the html from the url
        OkHttpClient client = new OkHttpClient();
        //request / response
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        return json;
    }

    public Token getToken(String username, String password) throws IOException{
        url = "http://54.209.183.244/moodle/login/token.php?username="+username+"&password=" + password +"&service=moodle_mobile_app";

        String json = baseCode();
        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Token> jsonAdapter = moshi.adapter(Token.class);
        Token token = jsonAdapter.fromJson(json);

        return token;
    }

    public Calendar getEvent() throws IOException{
        String apifunction = "&wsfunction=core_calendar_get_calendar_events";
        url += apifunction + "&moodlewsrestformat=json";

        String json = baseCode();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Calendar> jsonAdapter = moshi.adapter(Calendar.class);
        Calendar calendar = jsonAdapter.fromJson(json);

        return calendar;
    }

    public UserProfile run() throws IOException {
        String apifunction = "&wsfunction=core_webservice_get_site_info";
        url += apifunction + "&moodlewsrestformat=json";

        String json = baseCode();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UserProfile> jsonAdapter = moshi.adapter(UserProfile.class);
        UserProfile userProfile = jsonAdapter.fromJson(json);

        return userProfile;
    }

    public List<EnrolledCourse> runCours(int userId)throws IOException{
        String apifunction = "&wsfunction=core_enrol_get_users_courses";
        url += apifunction + "&userid=" + userId + "&moodlewsrestformat=json";
        String json = baseCode();
        Moshi moshi = new Moshi.Builder().build();

        Type enrolledCourseList = Types.newParameterizedType(List.class, EnrolledCourse.class);
        JsonAdapter<List<EnrolledCourse>> jsonAdapter = moshi.adapter(enrolledCourseList);
        List<EnrolledCourse> enrolledCourses  = jsonAdapter.fromJson(json);
        return enrolledCourses;
    }

    public List<CourseContent> getCourseContent(int courseId) throws IOException {
        String apifunction = "&wsfunction=core_course_get_contents";
        url += apifunction + "&courseid=" + courseId + "&moodlewsrestformat=json";

        String json = baseCode();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        Type courseContentList = Types.newParameterizedType(List.class, CourseContent.class);
        JsonAdapter<List<CourseContent>> jsonAdapter = moshi.adapter(courseContentList);
        List<CourseContent> courseContents = jsonAdapter.fromJson(json);

        return courseContents;
    }

}
