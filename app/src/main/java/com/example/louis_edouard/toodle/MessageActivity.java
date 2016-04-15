package com.example.louis_edouard.toodle;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.louis_edouard.toodle.moodle.Globals;
import com.example.louis_edouard.toodle.moodle.Message;
import com.example.louis_edouard.toodle.moodle.RootMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AdapterView.OnItemClickListener,
        CompoundButton.OnCheckedChangeListener{
    private View header;
    /************************/
    FloatingActionButton fabo;
    private ListView lvMessage;
    private MessageAdaptor messageAdaptor;
    private RootMessage rootMessage;
    private int userId;
    private Context messageConext = this;
    private SendMessageActivity sendMessageActivity;
    TextView drawer_txt_name;
    TextView drawer_txt_email;
    /****************delete*************/
    private boolean deleteMode;
    private int lvMessagesize;
    private List<Boolean> deleted;
    private ActionMode mActiveActionMode;
    private ActionMode.Callback mLastCallback;
    private boolean mInActionMode;
    /****************delete*************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = (View)navigationView.getHeaderView(0);
        /*******************************/
        /******* drawer menu ******/
        fabo = (FloatingActionButton) findViewById(R.id.fab);
        fabo.setOnClickListener(this);

        /**********************************************/

        lvMessage = (ListView)findViewById(R.id.lvMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {actionBar.setTitle("ListView");
            }
        }

        messageAdaptor = new MessageAdaptor(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        userId = pref.getInt(Globals.KEY_USER_ID, 0);
        RunAPI runAPI = new RunAPI();
        runAPI.execute();
        setTitle("Messagrie");
        lvMessage.setOnItemClickListener(this);

        /****************delete*************/
        lvMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });

        lvMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());
                onClick(checkBox);
                /////////  -->
                messageAdaptor.setDeleteMode(lvMessage);
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

        lvMessage.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lvMessage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
        /****************delete*************/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
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
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.pricipal_drawer_menu, menu);
//        return true;
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_home) {
            intent = new Intent(this,HomeDrawerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_course) {
            intent = new Intent(this,CoursActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(this,CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_message) {
            onBackPressed();
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
    /******************************************/
    /****************delete*************/
    class SomeCallback implements ActionMode.Callback{
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActiveActionMode = mode;
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_list_message,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //on va supprimer ou archiver
            if (item.getItemId()==R.id.delete){
                //traitmenet pour delete
            }
            setContentView(R.layout.activity_message);
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActiveActionMode = null;
            mInActionMode = false;
            for(int i=0;i<lvMessage.getChildCount();i++) {
                //Log.d("xyz", "getting child " + i);
                View v = lvMessage.getChildAt(i);
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
                deleteMode = false;
                cb.setChecked(false);
                cb.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("USER_ID_FROM", rootMessage.messages.get(position).useridfrom);
        intent.putExtra("USER_ID_TO", rootMessage.messages.get(position).useridto);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox cb=(CheckBox)buttonView;
        Toast.makeText(this, "check box " + isChecked, Toast.LENGTH_SHORT).show();
        int pos = ((Integer) cb.getTag()).intValue();
        Log.d("xyz", "clicked on checkbox tag " + pos + " checked=" + isChecked);
        deleted.set(pos, isChecked);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fab:
                intent = new Intent(this, SendMessageActivity.class);
                //intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                startActivity(intent);
                break;
            default:
                /***************delete*************/
                CheckBox cb = (CheckBox) v;
                Boolean isChecked = ((CheckBox) v).isChecked();
                Toast.makeText(this, "check box " + isChecked, Toast.LENGTH_LONG).show();
                int pos = ((Integer) v.getTag()).intValue();
                Log.d("xyz", "clicked on checkbox tag " + pos + " checked=" + isChecked);
                deleted.set(pos, isChecked);
                /***************delete*************/
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
    /****************delete*************/

    private class RunAPI extends AsyncTask<String, Object, RootMessage> {
        @Override
        protected RootMessage doInBackground(String... params) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            WebAPI web = new WebAPI(pref.getString(Globals.KEY_USER_TOKEN, null));

            try {
                rootMessage = web.getMessages(userId);

                Message message1, message2;
                for(int i = 0 ; i < rootMessage.messages.size(); i++){
                    message1 = rootMessage.messages.get(i);
                    for (int j = i + 1; j < rootMessage.messages.size(); j++) {
                        message2 = rootMessage.messages.get(j);
                        if ((message1.useridto == message2.useridto && message1.useridfrom == message2.useridfrom) ||
                                (message1.useridto == message2.useridfrom &&  message1.useridfrom == message2.useridto)) {
                            rootMessage.messages.remove(j);
                            j--;
                        }
                    }
                    if(i == rootMessage.messages.size()) break;
                }
            }
            catch(IOException e){ }

            return rootMessage;
        }

        @Override
        protected void onPostExecute(RootMessage message){
            super.onPostExecute(message);
            //messageAdaptor = new MessageAdaptor();
            lvMessage.setAdapter(messageAdaptor);

            /***************delete*************/
            messageAdaptor.setMode(Attributes.Mode.Single);
            lvMessagesize = rootMessage.messages.size();
            deleteMode = false;
            deleted = new ArrayList<Boolean>();
            for(int i=0; i<lvMessagesize; i++) deleted.add(false);
            /***************delete*************/
            drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
            drawer_txt_name.setText(HomeDrawerActivity.userName);
            drawer_txt_email = (TextView)header.findViewById(R.id.drawer_txt_email);
            //TODO: retrieve user's email address
            drawer_txt_email.setText(HomeDrawerActivity.userName + "@email.com");
        }
    }
    /////////MMMMM*********M
    private class MessageAdaptor extends BaseSwipeAdapter {
        private Context mContext;
        private LayoutInflater inflater;

        public MessageAdaptor(Context mContext) {
            this.mContext = mContext;
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            setTitle("Messagerie");
        }

//        public MessageAdaptor(MessageActivity messageActivity) {
//        }

        /***************delete*************/
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
            return R.id.swipeMessage;
        }

        /**
         * generate a new view item.
         * Never bind SwipeListener or fill values here, every item has a chance to fill value or bind
         * listeners in fillValues.
         * to fill it in {@code fillValues} method.
         *
         * @param position
         * @param parent
         * @return
         */
        @Override
        public View generateView(int position, ViewGroup parent) {
            View v=LayoutInflater.from(mContext).inflate(R.layout.listview_message, null);
            if (v == null) {
                v=LayoutInflater.from(mContext).inflate(R.layout.listview_message, null); // pour recuperer un layout et le mettre dans un view
            }
            /*******************************/

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
            cb.setOnCheckedChangeListener(MessageActivity.this);

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


            /*****************************/

            TextView person = (TextView)v.findViewById(R.id.txt_message_person);
            TextView time = (TextView)v.findViewById(R.id.txt_message_time);
            TextView description = (TextView)v.findViewById(R.id.txt_message_description);

            Message message = rootMessage.messages.get(position);
            person.setText(message.useridfrom == userId ? message.usertofullname : message.userfromfullname);
            description.setText(rootMessage.messages.get(position).text);
            time.setText(Globals.ConvertDate(message.timecreated));
            if(message.timeread == 0 && message.useridfrom != userId){
                description.setTextColor(Color.BLUE);
                time.setTextColor(Color.DKGRAY);
                time.setTypeface(null, Typeface.BOLD);
            }

            return v;
        }

        /**
         * fill values or bind listeners to the view.
         *
         * @param position
         * @param convertView
         */
        @Override
        public void fillValues(int position, View convertView) {
            TextView t = (TextView)convertView.findViewById(R.id.position);
            TextView t1 =(TextView)convertView.findViewById(R.id.trash);
//            String title = rootMessage.messages.get(position).usertofullname + " - " + rootMessage.messages.get(position).userfromfullname;
//            t.setText(title);
            // t1.setText(rootMessage.messages.get(position).usertofullname);
        }
        /***************delete*************/

        @Override
        public int getCount() {
            return rootMessage.messages.size();
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
