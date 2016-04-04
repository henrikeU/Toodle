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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.ArrayList;
import java.util.List;

public class CoursActivity extends AppCompatActivity
        implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,AdapterView.OnItemClickListener {
    List<EnrolledCourse> course;
    int userId;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private Context mContext = this;
    boolean deleteMode;
    int mListViewsize;
    List<Boolean> deleted;
    private ActionMode mActiveActionMode;
    private ActionMode.Callback mLastCallback;
    private boolean mInActionMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.listview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("ListView");
            }
        }

        mAdapter = new ListViewAdapter(this);
        // mListView.setAdapter(mAdapter); applique dans le postExecute

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

                CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());
                onClick(checkBox);
                /////////  -->
                mAdapter.setDeleteMode(mListView);
                mInActionMode = !mInActionMode;
                updateActionMode();
                ///////// <--
                if(mActiveActionMode!= null)
                    return  false;
                mActiveActionMode = startActionMode(mLastCallback);
                view.setSelected(true);
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

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    /* si tu le met il faut le deffinir si non, le back button ne fonctionne pas
    @Override
    public void onBackPressed() {
        //onCreate(new Bundle());
    }
    */

    //private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
    class SomeCallback implements ActionMode.Callback{
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActiveActionMode = mode;
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_list,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //on va supprimer ou archiver
            switch (item.getItemId()){
                case R.id.archive:
                    //tratiement pour archvie
                    break;
                case R.id.delete:
                    //traitmenet pour deleter
                    break;
            }
            setContentView(R.layout.activity_cours);
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActiveActionMode = null;
            mInActionMode = false;
            for(int i=0;i<mListView.getChildCount();i++) {
                //Log.d("xyz", "getting child " + i);
                View v = mListView.getChildAt(i);
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
                deleteMode = false;
                cb.setChecked(false);
                cb.setVisibility(View.GONE);
            }
        }
    };
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,CoursContentActivity.class);
        intent.putExtra("COURSE_ID", course.get(position).id);
        intent.putExtra("COURSE_TITLE",course.get(position).fullname);
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox cb=(CheckBox)buttonView;
        Toast.makeText(this,"check box "+isChecked,Toast.LENGTH_SHORT).show();
        int pos = ((Integer) cb.getTag()).intValue();
            Log.d("xyz", "clicked on checkbox tag "+pos + " checked=" +isChecked);
        deleted.set(pos,isChecked);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        CheckBox cb=(CheckBox)v;
        Boolean isChecked=((CheckBox) v).isChecked();
        Toast.makeText(this,"check box "+isChecked,Toast.LENGTH_LONG).show();
        int pos = ((Integer)v.getTag()).intValue();
        Log.d("xyz","clicked on checkbox tag "+pos+ " checked="+isChecked);
        deleted.set(pos,isChecked);
    }

    /*
    @Override
    public boolean onLongClick(View v) {
        CheckBox checkBox=(CheckBox)v.findViewById(R.id.checkBox);
        checkBox.setChecked(!checkBox.isChecked());
        onClick(checkBox);
        /////////  -->
        mAdapter.setDeleteMode(mListView);
        mInActionMode = !mInActionMode;
        updateActionMode();
        ///////// <--
        if(mActiveActionMode!= null)
            return  false;
        mActiveActionMode = startActionMode(mLastCallback);
        v.setSelected(true);
        return false;

    }
    */

    void updateActionMode(){
        if(!mInActionMode && mActiveActionMode !=null){
            mActiveActionMode.finish();
        }else if(mInActionMode && mActiveActionMode == null){
            if(mLastCallback==null) mLastCallback = new SomeCallback();
            //startActivity(mLastCallback);
        }
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
            mListViewsize = course.size();
            deleteMode = false;
            deleted = new ArrayList<Boolean>();
            for(int i=0; i<mListViewsize; i++) deleted.add(false);
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

        public void afficheMode(ListView lv) {
            for (int i = 0; i < lv.getChildCount(); i++) {
                //Log.d("xyz", "getting child " + i);
                View v = lv.getChildAt(i);
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
                if (deleteMode) {
                    cb.setVisibility(View.VISIBLE);
                } else {
                    cb.setVisibility(View.GONE);
                }
            }
        }

        public void setDeleteMode(ListView lv) {
            if( deleteMode==false ) {
                deleteMode=true;
                afficheMode(lv);
            }else {
                // si on veut on toggle

                deleteMode=false;
                afficheMode(lv);

            }
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            View v=LayoutInflater.from(mContext).inflate(R.layout.listview_item, null); ;

            if( v==null ) {
                v=LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
            }
//            TextView tv=(TextView)v.findViewById(R.id.textView);
//            tv.setText("item "+position);
            CheckBox cb=(CheckBox)v.findViewById(R.id.checkBox);

            if( deleteMode ) {
                cb.setVisibility(View.VISIBLE);
            }else{
                cb.setVisibility(View.GONE);
            }
            cb.setOnCheckedChangeListener(null);
            Log.d("xyz", "setting checkbox " + position + " to deleted=" + deleted.get(position));
            cb.setChecked(deleted.get(position));
            cb.setTag(new Integer(position));
//            cb.setOnClickListener(MainActivity.this);
            cb.setOnCheckedChangeListener(CoursActivity.this);

           // v.setOnLongClickListener(CoursActivity.this);

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
}