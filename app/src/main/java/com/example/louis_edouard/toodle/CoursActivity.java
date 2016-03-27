package com.example.louis_edouard.toodle;

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

import com.example.louis_edouard.toodle.moodle.Course;
import com.example.louis_edouard.toodle.moodle.Globals;

import java.io.IOException;

public class CoursActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    TextView txtNotifCours;
    ListView listViewCours;
    CoursAdapter coursAdapter;
    Course course;
    String courseTitle;

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

        //Intent intent = getIntent();
        txtNotifCours = (TextView)findViewById(R.id.txtNotifCours);
        listViewCours = (ListView)findViewById(R.id.listViewCours);

        coursAdapter = new CoursAdapter();
        listViewCours.setAdapter(coursAdapter);

        listViewCours.setOnItemClickListener(this);
        listViewCours.setOnItemLongClickListener(this);

        RunAPI run = new RunAPI();
        run.execute();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Item " + position, Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(this,CoursContentActivity.class);
//        String cours = donne[position];
//        intent.putExtra("coursTitle",cours);
//        startActivity(intent);
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
      *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    public class RunAPI extends AsyncTask<String, Object, Course> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Course doInBackground(String... params) {
            WebAPI web = new WebAPI(LoginActivity.userToken);

            try {
                course = web.runCours();
            }
            catch(IOException e){ }

            return course;
        }

        @Override
        protected void onPostExecute(Course course){
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

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return Globals.shortname.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vCours = convertView;
            if(vCours==null){
                vCours=inflaterCours.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)vCours.findViewById(android.R.id.text1);
            String title = Globals.shortname.get(position).shortname;
            text.setText(title);
            return vCours;
        }
    }
}
