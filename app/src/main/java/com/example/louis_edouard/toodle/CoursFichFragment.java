package com.example.louis_edouard.toodle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoursFichFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoursFichFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursFichFragment extends Fragment {
    TextView coursPlan, profName, dispoHrs,theoDys,tpDys;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //****Pour mettre le text_fragment sur chaque fragmenet suivi de ce qu'on a fait sur MainActivity from here to "return"
        View v = inflater.inflate(R.layout.fragment_cours_fich,container,false);
        coursPlan = (TextView)v.findViewById(R.id.txt_frag_cours_fich_plan);
        profName = (TextView)v.findViewById(R.id.txt_frag_cours_fich_prof);
        dispoHrs = (TextView)v.findViewById(R.id.txt_frag_cours_fich_dispoHrs);
        theoDys = (TextView)v.findViewById(R.id.txt_frag_cours_fich_theoDys);
        tpDys = (TextView)v.findViewById(R.id.txt_frag_cours_fich_tpDys);

//        Bundle args = getArguments();
//        int fragmentid = args.getInt("ID");
//        text.setText("Fragment "+ fragmentid);
        coursPlan.setText("Plan du cours");// TODO: a remplire correctement
        profName.setText("Sebastian Roy");// TODO: a remplire par le API
        dispoHrs.setText("Mardi 12:00 14:00");// TODO: a remplire par le API
        theoDys.setText("Lundi 10:30 - 12:30 /n Jeudi 10:30 - 11:30");// TODO: a remplire par le API
        tpDys.setText("Jeudi 11:30 - 13:30");// TODO: a remplire par le API
        return  v;
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