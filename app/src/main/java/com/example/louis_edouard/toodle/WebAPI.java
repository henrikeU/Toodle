package com.example.louis_edouard.toodle;

import android.content.Context;
import android.util.Log;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.Contact;
import com.example.louis_edouard.toodle.moodle.ContactRoot;
import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.Discussion;
import com.example.louis_edouard.toodle.moodle.DiscussionPost;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.Forum;
import com.example.louis_edouard.toodle.moodle.ForumDiscussion;
import com.example.louis_edouard.toodle.moodle.RootMessage;
import com.example.louis_edouard.toodle.moodle.Token;
import com.example.louis_edouard.toodle.moodle.UserProfile;
import com.example.louis_edouard.toodle.moodle.UserProfileSearch;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    DBHelper dbHelper = null;

    public WebAPI(){}

    public  WebAPI(String token){
        //url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=aa0c0e61965d7e3dc54c6fc8906fe442&wsfunction=core_course_get_courses&moodlewsrestformat=json";
        this.token = token;
        url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=" + token;
    }

    public  WebAPI(Context context, String token){
        //url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=aa0c0e61965d7e3dc54c6fc8906fe442&wsfunction=core_course_get_courses&moodlewsrestformat=json";
        this.token = token;
        url = "http://54.209.183.244/moodle/webservice/rest/server.php?wstoken=" + token;
        dbHelper = new DBHelper(context);
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

    public Calendar getEvent(int userid) throws IOException{
        List<EnrolledCourse> enrolledCourses = getCourse(userid);
        String apifunction = "core_calendar_get_calendar_events";
        String values = "";
        for(int i = 0; i< enrolledCourses.size(); i++)
            values += "events[eventids]["+i+"]=" + enrolledCourses.get(i).id + "&";
        url = fullUrl(apifunction) + "&" + values;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Calendar> jsonAdapter = moshi.adapter(Calendar.class);
        Calendar calendar = jsonAdapter.fromJson(getJSON());

        dbHelper.addEvents(calendar.events);

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

    public List<UserProfileSearch> getUser(List<Integer> userIds) throws IOException{
        String apifunction = "&wsfunction=core_user_get_users_by_field";
        String values = "";
        for(int i = 0; i< userIds.size(); i++)
            values += "values["+i+"]=" + userIds.get(i) + "&";
        url = fullUrl(apifunction) + "&field=id&" + values;

        Moshi moshi = new Moshi.Builder().build();
        Type userProfileSearchList = Types.newParameterizedType(List.class, UserProfileSearch.class);
        JsonAdapter<List<UserProfileSearch>> jsonAdapter = moshi.adapter(userProfileSearchList);
        List<UserProfileSearch> userProfileSearches  = jsonAdapter.fromJson(getJSON());

        return userProfileSearches;
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
        String apifunction = "&wsfunction=core_message_get_messages";
        url = fullUrl(apifunction) + "&useridto=" + userId1 + "&useridfrom=" + userId2 + "&read=" + read;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<RootMessage> jsonAdapter = moshi.adapter(RootMessage.class);

        RootMessage message = jsonAdapter.fromJson(getJSON());

        return message;
    }

    public List<EnrolledCourse> getCourse(int userId)throws IOException{
        String apifunction = "&wsfunction=core_enrol_get_users_courses";
        url = fullUrl(apifunction) + "&userid=" + userId;

        Moshi moshi = new Moshi.Builder().build();
        Type enrolledCourseList = Types.newParameterizedType(List.class, EnrolledCourse.class);
        JsonAdapter<List<EnrolledCourse>> jsonAdapter = moshi.adapter(enrolledCourseList);
        List<EnrolledCourse> enrolledCourses  = jsonAdapter.fromJson(getJSON());

        dbHelper.addCourses(enrolledCourses);

        return enrolledCourses;
    }

    public int updateCours(int userId)throws IOException{
        String apifunction = "core_enrol_get_users_courses";
        url = fullUrl(apifunction) + "&userid=" + userId;

        Moshi moshi = new Moshi.Builder().build();
        Type enrolledCourseList = Types.newParameterizedType(List.class, EnrolledCourse.class);
        JsonAdapter<List<EnrolledCourse>> jsonAdapter = moshi.adapter(enrolledCourseList);
        List<EnrolledCourse> enrolledCourses  = jsonAdapter.fromJson(getJSON());

        int nb = dbHelper.addCourses(enrolledCourses);

        return nb;
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

    public List<Forum> getForums(int courseid) throws IOException{
        String apifunction = "mod_forum_get_forums_by_courses";
        url = fullUrl(apifunction) + "&courseids[0]=" + courseid;

        Moshi moshi = new Moshi.Builder().build();
        Type forumList = Types.newParameterizedType(List.class, Forum.class);
        JsonAdapter<List<Forum>> jsonAdapter = moshi.adapter(forumList);
        List<Forum> forums = jsonAdapter.fromJson(getJSON());

        return forums;
    }

    public ForumDiscussion getDiscussions(int forumid) throws IOException{
        String apifunction = "mod_forum_get_forum_discussions_paginated";
        url = fullUrl(apifunction) + "&forumid=" + forumid;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ForumDiscussion> jsonAdapter = moshi.adapter(ForumDiscussion.class);
        ForumDiscussion forumDiscussion = jsonAdapter.fromJson(getJSON());

        return forumDiscussion;
    }

    public DiscussionPost getPosts(int discussionid) throws IOException{
        String apifunction = "mod_forum_get_forum_discussion_posts";
        url = fullUrl(apifunction) + "&discussionid=" + discussionid;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<DiscussionPost> jsonAdapter = moshi.adapter(DiscussionPost.class);
        DiscussionPost discussionPost = jsonAdapter.fromJson(getJSON());

        return discussionPost;

    }

    public ContactRoot getContacts() throws IOException{
        String apifunction = "core_message_get_contacts";
        url = fullUrl(apifunction);

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ContactRoot> jsonAdapter = moshi.adapter(ContactRoot.class);
        ContactRoot contactRoot = jsonAdapter.fromJson(getJSON());

        List<Integer> userIds = new ArrayList<>();
        for (Contact c: contactRoot.offline)
            userIds.add(c.id);
        for (Contact c: contactRoot.online)
            userIds.add(c.id);

        List<UserProfileSearch> users = getUser(userIds);
        dbHelper.addContacts(users);
        return contactRoot;
    }
}
