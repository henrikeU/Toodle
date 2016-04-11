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

import com.example.louis_edouard.toodle.moodle.CalendarEvent;
import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.Forum;
import com.example.louis_edouard.toodle.moodle.Globals;

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

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return null;
        }

        /**
         * Return the number of views available.
         */
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
//    public CoursForumFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CoursForumFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CoursForumFragment newInstance(String param1, String param2) {
//        CoursForumFragment fragment = new CoursForumFragment();
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
//        return inflater.inflate(R.layout.fragment_cours_forum, container, false);
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
