package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userId = pref.getInt(Globals.KEY_USER_ID, 0);

        txtNotifCours = (TextView)findViewById(R.id.txtNotifCours);
        listViewCours = (ListView)findViewById(R.id.listViewCours);
        registerForContextMenu(listViewCours);
        //listViewCours.setOnItemLongClickListener(this);
        RunAPI run = new RunAPI();
        run.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,CoursContentActivity.class);
        intent.putExtra("COURSE_ID", course.get(position).id);
        intent.putExtra("COURSE_TITLE",course.get(position).fullname);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listViewCours) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {


            case R.id.archive:
                // archive stuff here
                return true;
            case R.id.delete:
                // delete stuff here

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),"Item "+position,Toast.LENGTH_SHORT).show();
//        LayoutInflater inflater=null;
//        View v = view;
//        if (v == null) {
//            v = inflater.inflate(R.layout.row_delete_archive, parent, false); // pour recuperer un layout et le mettre dans un view
//        }
//        TextView tv = (TextView)v.findViewById(R.id.txt_row_del_arch);
//        CheckBox cb = (CheckBox)v.findViewById(R.id.checkBox_row_del_arch);
//        String title = course.get(position).shortname;
//
//        tv.setText(title);
//        return true;
//    }

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
