package com.example.louis_edouard.toodle;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis_edouard.toodle.moodle.Calendar;
import com.example.louis_edouard.toodle.moodle.Globals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,AdapterView.OnItemClickListener {
    ListView lvCalendar;
    ListAdapter listAdapter;
    Calendar calendar;
    TextView interval;
    private Context mContext = this;
    private boolean deleteMode;
    private int mListViewsize;
    private List<Boolean> deleted;
    private ActionMode mActiveActionMode;
    private ActionMode.Callback mLastCallback;
    private boolean mInActionMode;
    private View header;
    TextView drawer_txt_name;
    TextView drawer_txt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*******/
        lvCalendar = (ListView)findViewById(R.id.lv_calendar);
        listAdapter = new ListAdapter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {actionBar.setTitle("ListView");
            }
        }
        header = (View)navigationView.getHeaderView(0);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();

        lvCalendar.setOnItemClickListener(this);
        /*
        lvCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        lvCalendar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());
                onClick(checkBox);
                /////////  -->
                listAdapter.setDeleteMode(lvCalendar);
                mInActionMode = !mInActionMode;
                updateActionMode();
                ///////// <--
                if (mActiveActionMode != null)
                    return false;
                mActiveActionMode = startActionMode(mLastCallback);
                view.setSelected(true);
                return true;
            }
        });

        lvCalendar.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lvCalendar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.add_event) {
                Intent intent = new Intent(this, CalendarAddEventActivity.class);
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home) {
            intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_course) {
            intent = new Intent(this,CoursActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            onBackPressed();
        } else if (id == R.id.nav_message) {
            intent = new Intent(this,MessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
//            intent = new Intent(this,ContactActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_send) {
            intent = new Intent(this, SendMessageActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class SomeCallback implements ActionMode.Callback{
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActiveActionMode = mode;
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_list, menu);
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
            setContentView(R.layout.activity_calendar_drawer);
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActiveActionMode = null;
            mInActionMode = false;
            for(int i=0;i<lvCalendar.getChildCount();i++) {
                //Log.d("xyz", "getting child " + i);
                View v = lvCalendar.getChildAt(i);
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
                deleteMode = false;
                cb.setChecked(false);
                cb.setVisibility(View.GONE);
            }
        }
    };

        /******************/
    private class ListAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflaterClendar;

        public ListAdapter(Context mContext){
            this.mContext = mContext;
            inflaterClendar= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
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
    /*
        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }
    */
    /*
        @Override
        public View generateView(int position, ViewGroup parent) {
            View v=LayoutInflater.from(mContext).inflate(R.layout.listview_calendar, null);

            if( v==null ) {
                v=LayoutInflater.from(mContext).inflate(R.layout.listview_calendar, null);
            }



            TextView text = (TextView)v.findViewById(android.R.id.text1);
           text.setText(calendar.events.get(position).name);


            TextView tv=(TextView)v.findViewById(R.id.position);
            tv.setText("item "+position);
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
            cb.setOnClickListener(MainActivity.this);
            cb.setOnCheckedChangeListener(CalendarActivity.this);

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
            TextView t = (TextView) convertView.findViewById(R.id.position);
            TextView t1 =(TextView)convertView.findViewById(R.id.trash);
            String title = calendar.events.get(position).name + " - " + calendar.events.get(position).description;
            t.setText(title);
            t1.setText(calendar.events.get(position).name);
        }
    */
        @Override
        public int getCount() {
            return calendar.events.size();
        }

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
            View view = convertView;
            if(view==null){
                view=inflaterClendar.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)view.findViewById(android.R.id.text1);
            text.setText(calendar.events.get(position).name);
            return view;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CalendarPrivateActivity.class);
//        intent.putExtra("USER_ID_FROM", rootMessage.messages.get(position).useridfrom);
//        intent.putExtra("USER_ID_TO", rootMessage.messages.get(position).useridto);
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox cb=(CheckBox)buttonView;
        Toast.makeText(this, "check box " + isChecked, Toast.LENGTH_SHORT).show();
        int pos = ((Integer) cb.getTag()).intValue();
        Log.d("xyz", "clicked on checkbox tag "+pos + " checked=" +isChecked);
        deleted.set(pos,isChecked);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_clndr_private_list:
                //TODO
                break;
            case R.id.btn_clndr_public_invite:
                //TODO
                break;
            default:
                CheckBox cb = (CheckBox) v;
                Boolean isChecked = ((CheckBox) v).isChecked();
                Toast.makeText(this, "check box " + isChecked, Toast.LENGTH_LONG).show();
                int pos = ((Integer) v.getTag()).intValue();
                Log.d("xyz", "clicked on checkbox tag " + pos + " checked=" + isChecked);
                deleted.set(pos, isChecked);
        }
    }


    void updateActionMode(){
        if(!mInActionMode && mActiveActionMode !=null){
            mActiveActionMode.finish();
        }else if(mInActionMode && mActiveActionMode == null){
            if(mLastCallback==null) mLastCallback = new SomeCallback();
            //startActivity(mLastCallback);
        }
    }
    public class RunAPI extends AsyncTask<String, Object, Calendar> {

        @Override
        protected void onPostExecute(Calendar calendar) {
            super.onPostExecute(calendar);
            lvCalendar.setAdapter(listAdapter);
           // listAdapter.setMode(Attributes.Mode.Single);
            mListViewsize = calendar.events.size();
            /////////**************//////////***********//////////////
            deleteMode = false;
            deleted = new ArrayList<Boolean>();
            for(int i=0; i<mListViewsize; i++) deleted.add(false);

            drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
            drawer_txt_name.setText(HomeActivity.userFullName);

        }

        @Override
        protected Calendar doInBackground(String... params) {
            // recuperer le token de l'utilisateur
            SharedPreferences preferences = getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

            WebAPI webAPI = new WebAPI(preferences.getString(Globals.KEY_USER_TOKEN, null));
            try {
                calendar = webAPI.getEvent();
            }
            catch(IOException e){ }

            return calendar;
        }
    }
}
