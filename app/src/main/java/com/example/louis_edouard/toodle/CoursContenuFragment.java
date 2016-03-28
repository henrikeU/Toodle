package com.example.louis_edouard.toodle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.CourseContent;
import com.example.louis_edouard.toodle.moodle.Globals;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;


public class CoursContenuFragment extends Fragment implements AdapterView.OnItemClickListener {
    TextView currentWeek, weekDecription;
    ListView lsvPdf, lsvPrvsWeeks;
    CoursAdapter coursAdapter;
    List<CourseContent> courseContents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_cours_contenu,container,false);
        currentWeek = (TextView)v.findViewById(R.id.txt_frag_cours_cntnu_currentWeek);
        weekDecription = (TextView)v.findViewById(R.id.txt_frag_cours_cntnu_weekDscrpt);
        //TODO: getting information from API for these text views above
        lsvPdf=(ListView)v.findViewById(R.id.lsv_frag_cours_cntnu_pdf);
        lsvPrvsWeeks = (ListView)v.findViewById(R.id.lsv_frag_cours_cntun_prvsWeeks);
        //TODO: filling these list views above
        currentWeek.setText("this week");
        weekDecription.setText("content of this week");
        RunAPI run = new RunAPI();
        run.execute();
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class CoursCntntFragAdaptor extends FragmentPagerAdapter {

        public CoursCntntFragAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) { //TODO:get items for two listviews
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

    public class RunAPI extends AsyncTask<String, Object, List<CourseContent>> {

        @Override
        protected List<CourseContent> doInBackground(String... params) {
            WebAPI web = new WebAPI(CoursContentActivity.USER_TOKEN);
            try {
                courseContents = web.getCourseContent(CoursContentActivity.COURSE_ID);
            }
            catch(IOException e){ }

            return courseContents;
        }

        @Override
        protected void onPostExecute(List<CourseContent> courseContents){
            super.onPostExecute(courseContents);
            coursAdapter = new CoursAdapter();
            lsvPrvsWeeks.setAdapter(coursAdapter);
            //for make clickable the links
            lsvPrvsWeeks.setOnItemClickListener(CoursContenuFragment.this);//implements onItemClick
        }
    }

    private class CoursAdapter extends BaseAdapter {
        LayoutInflater inflaterCours;

        public CoursAdapter() {
            inflaterCours= (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() { return courseContents.size(); }

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
            View vCours = convertView;
            if(vCours==null){
                vCours=inflaterCours.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)vCours.findViewById(android.R.id.text1);
            String title = courseContents.get(position).name;
            text.setText(title);
            return vCours;
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
