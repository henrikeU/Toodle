package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AccuilActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView lv;
    public final int n = 30; // ca devrait etre egale la taille reelle de l'evenements
    Button btnCours,btnMess,btnCalend,btnTous;
    //pour remplir le listView on utilise un adaptor
    LVAdapter adapter;
    public String[] donne = {"Matiar","Rania", "Ronna","Arian", "Iris" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accuil);

        lv = (ListView)findViewById(R.id.listv);

        btnCours = (Button)findViewById(R.id.cours);
        btnMess = (Button)findViewById(R.id.messagerie);
        btnCalend = (Button)findViewById(R.id.calendrier);
        btnTous = (Button)findViewById(R.id.tous);
        btnCours.setOnClickListener(this);
        btnMess.setOnClickListener(this);
        btnCalend.setOnClickListener(this);
        btnTous.setOnClickListener(this);

        adapter = new LVAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MainActivity.class);

        switch (v.getId()) {
            case R.id.cours:
                intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.calendrier:
                intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.messagerie:
                intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.tous:
                intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
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
        Intent intent = new Intent(this,DetailsActivity.class);
        String name = donne[position];
        intent.putExtra("name",name);
        startActivity(intent);
    }


    private class LVAdapter extends BaseAdapter {
        LayoutInflater lf;
        public LVAdapter() {
            lf= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return donne.length;//la propriete final n = 30
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
            View v = convertView;
            if(v==null){
                v=lf.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)v.findViewById(android.R.id.text1);
            text.setText(donne[position]);
            return v;
        }
    }
}
