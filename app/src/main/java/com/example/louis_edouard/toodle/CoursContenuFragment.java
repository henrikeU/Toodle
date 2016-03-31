package com.example.louis_edouard.toodle;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.io.IOException;
import java.util.List;


public class CoursContenuFragment extends Fragment implements AdapterView.OnItemClickListener {
    TextView alert;
    ExpandableListView pastWeeks;
    PastWeekAdapter pastWeekAdapter;
    List<CourseContent> courseContents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_cours_contenu,container,false);
        alert = (TextView)v.findViewById(R.id.textview_alert);
        //TODO: getting information from API for these text views above
        alert.setText("Du nouveau contenu!");

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
        public Object getChild(int groupPosition, int childPosition) {
            return courseContents.get(groupPosition).modules.get(childPosition).name;
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
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public CoursContenuFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CoursContenuFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CoursContenuFragment newInstance(String param1, String param2) {
//        CoursContenuFragment fragment = new CoursContenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cours_contenu, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
