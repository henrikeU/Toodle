package com.example.louis_edouard.toodle;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.CourseContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class CoursFichFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    TextView coursPlan, prof, theoDys, tpDys;
    List<CourseContent> courseContents;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //****Pour mettre le text_fragment sur chaque fragmenet suivi de ce qu'on a fait sur MainActivity from here to "return"
        View v = inflater.inflate(R.layout.fragment_cours_fich,container,false);

        coursPlan = (TextView)v.findViewById(R.id.txt_frag_cours_fich_plan);
        prof = (TextView)v.findViewById(R.id.txt_frag_cours_fich_prof);
        theoDys = (TextView)v.findViewById(R.id.txt_frag_cours_fich_theoDys);
        tpDys = (TextView)v.findViewById(R.id.txt_frag_cours_fich_tpDys);

        coursPlan.setOnClickListener(this);

        RunAPI run = new RunAPI();
        run.execute();
        return  v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"Download plan de cours?",Toast.LENGTH_LONG).show();
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
            String html = courseContents.get(0).summary;
            Document doc = Jsoup.parseBodyFragment(html);
            Element teacher  = doc.getElementsByClass("teacher").first();
            Elements theories = doc.getElementsByClass("theorie");
            Elements tps = doc.getElementsByClass("horaire-tp");
            String horaireTheorie = "";
            String horaireTp = "";
            for(Element horaire : theories){
                horaireTheorie += horaire.text() + "\n";
            }
            for(Element horaire: tps){
                horaireTp += horaire.text() + "\n";
            }
            prof.setText(teacher.text());
            theoDys.setText(horaireTheorie.substring(0, horaireTheorie.length() - 1));
            tpDys.setText(horaireTp.substring(0, horaireTp.length() - 1));

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
//    public CoursContentFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CoursContentFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CoursContentFragment newInstance(String param1, String param2) {
//        CoursContentFragment fragment = new CoursContentFragment();
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
//        return inflater.inflate(R.layout.fragment_cours_fich, container, false);
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
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
