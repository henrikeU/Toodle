package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.Globals;

import java.io.IOException;
import java.util.List;

public class CoursActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    TextView txtNotifCours;
    ListView listViewCours;
    CoursAdapter coursAdapter;
    List<EnrolledCourse> course;
    String courseTitle;
    int userId;

    private String[] donne = {"IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique",
            "IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique",
            "IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique",
            "IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique",
            "IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique","IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique",
            "IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique",
            "IFT2905 Interface person-machine",
            "IFT1025 Programmation 2",
            "IFT2105 Introduction a l'informatique theorique"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userId = pref.getInt(Globals.KEY_USER_ID, 0);

        txtNotifCours = (TextView)findViewById(R.id.txtNotifCours);
        listViewCours = (ListView)findViewById(R.id.listViewCours);

        RunAPI run = new RunAPI();
        run.execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,CoursContentActivity.class);
        String coursTitle = course.get(position).fullname;
        intent.putExtra("coursTitle",coursTitle);
        startActivity(intent);
    }

    //@Override
    //public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
     //   return false;
    //}

    public class RunAPI extends AsyncTask<String, Object, List<EnrolledCourse>> {

        @Override
        protected List<EnrolledCourse> doInBackground(String... params) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));

            try {
                course = web.runCours(userId);
            }
            catch(IOException e){ }

            return course;
        }

        @Override
        protected void onPostExecute(List<EnrolledCourse> course){
            super.onPostExecute(course);
            coursAdapter = new CoursAdapter();
            listViewCours.setAdapter(coursAdapter);
            //for make clickable the links
            listViewCours.setOnItemClickListener(CoursActivity.this);//implements onItemClick
            // listViewCours.setOnItemLongClickListener(this);
        }
    }

    private class CoursAdapter extends BaseAdapter {
        LayoutInflater inflaterCours;

        public CoursAdapter() {
            inflaterCours= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            setTitle("Cours");
        }

        @Override
        public int getCount() { return course.size(); }

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
            String title = course.get(position).shortname;
            text.setText(title);
            return vCours;
        }
    }

}
