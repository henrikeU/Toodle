package com.example.louis_edouard.toodle;

import android.util.Log;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.RootMessage;
import com.example.louis_edouard.toodle.moodle.Token;
import com.example.louis_edouard.toodle.moodle.UserProfile;
import com.example.louis_edouard.toodle.moodle.UserProfileSearch;
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
    public final String baseUrl = "http://54.209.183.244/moodle/webservice/rest/server.php?";
    private String token;
    public int id;

    public WebAPI(){}

    public  WebAPI(String token){
        //url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=aa0c0e61965d7e3dc54c6fc8906fe442&wsfunction=core_course_get_courses&moodlewsrestformat=json";
        this.token = token;
        url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=" + token;
    }

    private String getJSON() throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        return json;
    }

    private String fullUrl(String apifunction){
        String wstoken = "wstoken=" + token;
        String wsfunction = "&wsfunction=" + apifunction;
        String restformat = "&moodlewsrestformat=json";
        String url = baseUrl + wstoken + wsfunction + restformat;
        return url;
    }

    public Token getToken(String username, String password) throws IOException{
        url = "http://54.209.183.244/moodle/login/token.php?username="+username+"&password=" + password +"&service=moodle_mobile_app";

        String json = getJSON();
        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Token> jsonAdapter = moshi.adapter(Token.class);
        Token token = jsonAdapter.fromJson(json);

        return token;
    }

    public Calendar getEvent() throws IOException{
        String apifunction = "core_calendar_get_calendar_events";
        url = fullUrl(apifunction);

        String json = getJSON();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Calendar> jsonAdapter = moshi.adapter(Calendar.class);
        Calendar calendar = jsonAdapter.fromJson(json);

        return calendar;
    }

    public UserProfile getUserProfile() throws IOException {
        String apifunction = "core_webservice_get_site_info";
        url = fullUrl(apifunction);

        String json = getJSON();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UserProfile> jsonAdapter = moshi.adapter(UserProfile.class);
        UserProfile userProfile = jsonAdapter.fromJson(json);
        return userProfile;
    }

    public RootMessage getMessages(int useridto) throws IOException {
        return getMessages(useridto, 0);
    }

    public RootMessage getMessages(int useridto, int useridfrom) throws IOException {
        RootMessage message = getMessage(useridto, useridfrom, 0);
        message.messages.addAll(getMessage(useridfrom, useridto, 0).messages);
        message.messages.addAll(getMessage(useridto, useridfrom, 1).messages);
        message.messages.addAll(getMessage(useridfrom, useridto, 1).messages);
        return message;
    }

    private RootMessage getMessage(int userId1, int userId2, int read) throws IOException {
        String baseUrl = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=" + token;
        String apifunction = "&wsfunction=core_message_get_messages";

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<RootMessage> jsonAdapter = moshi.adapter(RootMessage.class);

        url = baseUrl + apifunction + "&useridto=" + userId1 + "&useridfrom=" + userId2 + "&read=" + read + "&moodlewsrestformat=json";

        RootMessage message = jsonAdapter.fromJson(getJSON());

        return message;
    }

    public List<UserProfileSearch> getUser(List<Integer> userIds) throws IOException{
        url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=" + token;
        String apifunction = "&wsfunction=core_user_get_users_by_field";
        String values ="";
        for(int i = 0; i< userIds.size(); i++)
            values += "values["+i+"]=" + userIds.get(i) + "&";
        url += apifunction + "&field=id&" + values + "moodlewsrestformat=json";
        Log.d("URL", url);
        String json = getJSON();

        Moshi moshi = new Moshi.Builder().build();
        Type userProfileSearchList = Types.newParameterizedType(List.class, UserProfileSearch.class);
        JsonAdapter<List<UserProfileSearch>> jsonAdapter = moshi.adapter(userProfileSearchList);
        List<UserProfileSearch> userProfileSearches  = jsonAdapter.fromJson(json);
        return userProfileSearches;
    }

    public List<EnrolledCourse> runCours(int userId)throws IOException{
        String apifunction = "&wsfunction=core_enrol_get_users_courses";
        url += apifunction + "&userid=" + userId + "&moodlewsrestformat=json";
        String json = getJSON();
        Moshi moshi = new Moshi.Builder().build();

        Type enrolledCourseList = Types.newParameterizedType(List.class, EnrolledCourse.class);
        JsonAdapter<List<EnrolledCourse>> jsonAdapter = moshi.adapter(enrolledCourseList);
        List<EnrolledCourse> enrolledCourses  = jsonAdapter.fromJson(json);
        return enrolledCourses;
    }

    public List<CourseContent> getCourseContent(int courseId) throws IOException {
        String apifunction = "&wsfunction=core_course_get_contents";
        url += apifunction + "&courseid=" + courseId + "&moodlewsrestformat=json";

        String json = getJSON();

        // parse JSON content from the string
        Moshi moshi = new Moshi.Builder().build();
        Type courseContentList = Types.newParameterizedType(List.class, CourseContent.class);
        JsonAdapter<List<CourseContent>> jsonAdapter = moshi.adapter(courseContentList);
        List<CourseContent> courseContents = jsonAdapter.fromJson(json);

        return courseContents;
    }

}
