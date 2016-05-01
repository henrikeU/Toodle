package com.example.louis_edouard.toodle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.CourseModule;

import java.io.IOException;
import java.util.List;


public class CoursContenuFragment extends Fragment implements AdapterView.OnItemClickListener {
    ExpandableListView pastWeeks;
    PastWeekAdapter pastWeekAdapter;
    List<CourseContent> courseContents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_cours_contenu,container,false);

        pastWeeks = (ExpandableListView)v.findViewById(R.id.expandableListView_pastWeeks);
        RunAPI run = new RunAPI();
        run.execute();
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class RunAPI extends AsyncTask<String, Object, List<CourseContent>> {

        @Override
        protected List<CourseContent> doInBackground(String... params) {
            WebAPI web = new WebAPI(CoursContentActivity.USER_TOKEN);
            try {
                courseContents = web.getCourseContent(CoursContentActivity.COURSE_ID);
                courseContents.remove(0); // supprime la première section (FICHE)
            }
            catch(IOException e){ }

            return courseContents;
        }

        @Override
        protected void onPostExecute(List<CourseContent> courseContents){
            super.onPostExecute(courseContents);
            pastWeekAdapter = new PastWeekAdapter();
            pastWeeks.setAdapter(pastWeekAdapter);

            //TODO: Calculer la position de la semaine courante
            int currentWeekPosition = courseContents.size() - 1;
            // ouvre et focus sur la semaine courante
            pastWeeks.expandGroup(currentWeekPosition);
            pastWeeks.setSelectedGroup(currentWeekPosition);
        }
    }

    private class PastWeekAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return courseContents.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return courseContents.get(groupPosition).modules.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return courseContents.get(groupPosition).name;
        }

        @Override
        public CourseModule getChild(int groupPosition, int childPosition) {
            return courseContents.get(groupPosition).modules.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String)getGroup(groupPosition);
            if(convertView == null){
                LayoutInflater infalInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            TextView lblListHeader = (TextView)convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle);
            if(isExpanded)
                lblListHeader.setTypeface(null, Typeface.BOLD);
            else
                lblListHeader.setTypeface(null, Typeface.NORMAL);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final CourseModule module =getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            TextView txtListDescription = (TextView) convertView.findViewById(R.id.lblListDescription);

            txtListChild.setText(module.name);
            if (module.description == null)
                txtListDescription.setVisibility(View.GONE);
            else{
                txtListDescription.setVisibility(View.VISIBLE);
                txtListDescription.setText(Globals.HtmlToText(module.description));
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
