package com.example.louis_edouard.toodle;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.example.louis_edouard.toodle.moodle.EnrolledCourse;
import com.example.louis_edouard.toodle.moodle.Globals;

import java.io.IOException;
import java.util.List;

public class CoursActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView txtNotifCours;
    List<EnrolledCourse> course;
    String courseTitle;
    CheckBox checkBox;
    boolean showCheckBox = false;
    int userId;

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);
        mListView = (ListView) findViewById(R.id.listview);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("ListView");
            }
        }

        mAdapter = new ListViewAdapter(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userId = pref.getInt(Globals.KEY_USER_ID, 0);

        RunAPI run = new RunAPI();
        run.execute();


        mListView.setOnItemClickListener(this);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                showCheckBox = !showCheckBox;

                    if(showCheckBox){
                        findViewById(R.id.checkBox).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.checkBox).setVisibility(View.GONE);
                    }

                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,CoursContentActivity.class);
        intent.putExtra("COURSE_ID", course.get(position).id);
        intent.putExtra("COURSE_TITLE",course.get(position).fullname);
        startActivity(intent);
    }

    private class RunAPI extends AsyncTask<String, Object, List<EnrolledCourse>> {
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
            mListView.setAdapter(mAdapter);
            mAdapter.setMode(Attributes.Mode.Single);
        }
    }

    private class ListViewAdapter extends BaseSwipeAdapter {

        private Context mContext;
        private LayoutInflater layoutInflater;

        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
            layoutInflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            setTitle("Cours");
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);

            if(showCheckBox){
                v.findViewById(R.id.checkBox).setVisibility(View.VISIBLE);
            }else{
                v.findViewById(R.id.checkBox).setVisibility(View.GONE);
            }

            SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    //YoYo.with(Techniques.SlideInRight).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });

            v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                }
            });

            v.findViewById(R.id.archive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "click archive", Toast.LENGTH_SHORT).show();
                }
            });


            return v;
        }

        @Override
        public void fillValues(int position, View convertView) {
            TextView t = (TextView)convertView.findViewById(R.id.position);
            TextView t1 =(TextView)convertView.findViewById(R.id.trash);
            String title = course.get(position).shortname + " - " + course.get(position).fullname;
            t.setText(title);
            t1.setText(course.get(position).shortname);
        }

        @Override
        public int getCount() {
            return course.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
       // if (v.getId()==R.id.listViewCours) {
         //   MenuInflater inflater = getMenuInflater();
           // inflater.inflate(R.menu.menu_list, menu);
       // }
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
*/
}