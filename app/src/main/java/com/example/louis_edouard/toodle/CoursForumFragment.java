package com.example.louis_edouard.toodle;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.Forum;

import java.io.IOException;
import java.util.List;


public class CoursForumFragment extends Fragment {
    ListView forumListView;
    List<Forum> forums;
    ForumAdapter forumAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cours_forum, container, false);
        //TODO: filling the list view of forum
        forumListView = (ListView)v.findViewById(R.id.lsv_frag_cours_forum);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
        return v;
    }

    private class CoursForumFragAdaptor extends FragmentPagerAdapter {

        public CoursForumFragAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    private class ForumAdapter extends BaseAdapter {
        LayoutInflater inflaterHome;

        public ForumAdapter() {
            inflaterHome= (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() { return forums.size();  }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vHome = convertView;
            if(vHome==null){
                vHome=inflaterHome.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)vHome.findViewById(android.R.id.text1);
            text.setText(forums.get(position).name);
            return vHome;
        }
    }

    public class RunAPI extends AsyncTask<String, Object, List<Forum>> {

        @Override
        protected List<Forum> doInBackground(String... params) {
            WebAPI web = new WebAPI(CoursContentActivity.USER_TOKEN);
            try {
                Log.d("COURSEID", CoursContentActivity.COURSE_ID + "");
                forums = web.getForums(CoursContentActivity.COURSE_ID);
            }
            catch(IOException e){ }

            return forums;
        }

        @Override
        protected void onPostExecute(List<Forum> forums){
            super.onPostExecute(forums);
            forumAdapter = new ForumAdapter();
            forumListView.setAdapter(forumAdapter);
        }
    }
}
