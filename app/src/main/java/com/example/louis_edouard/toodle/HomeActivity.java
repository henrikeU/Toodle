package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;


import com.example.louis_edouard.toodle.moodle.Globals;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView listViewHome;
    Button btnCoursHome,btnMessHome,btnCalendHome,btnTousHome;
    //pour remplir le listView on utilise un adaptor
    HomeAdapter homeAdapter;

    private String[] donne = {"Cours IFT2905 dans 10 mins",
            "Examen Intra de IFT1025 dans une semaine",
            "La date limite pour abandonner avec frais dans 2 semaines",
            "Cours IFT2905 dans 10 mins",
            "Examen Intra de IFT1025 dans une semaine",
            "La date limite pour abandonner avec frais dans 2 semaines",
            "Cours IFT2905 dans 10 mins",
            "Examen Intra de IFT1025 dans une semaine",
            "La date limite pour abandonner avec frais dans 2 semaines",
            "Cours IFT2905 dans 10 mins",
            "Examen Intra de IFT1025 dans une semaine",
            "La date limite pour abandonner avec frais dans 2 semaines",
            "Cours IFT2905 dans 10 mins",
            "Examen Intra de IFT1025 dans une semaine",
            "La date limite pour abandonner avec frais dans 2 semaines"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        //String name = intent.getStringExtra("USER_NAME");
        String name = pref.getString(Globals.KEY_USER_NAME, null);

        listViewHome = (ListView)findViewById(R.id.listViewHome);

        btnCoursHome = (Button)findViewById(R.id.btnCoursHome);
        btnMessHome = (Button)findViewById(R.id.btnMessHome);
        btnCalendHome = (Button)findViewById(R.id.btnCalendHome);
        btnTousHome = (Button)findViewById(R.id.btnTousHome);
        btnCoursHome.setOnClickListener(this);
        btnMessHome.setOnClickListener(this);
        btnCalendHome.setOnClickListener(this);
        btnTousHome.setOnClickListener(this);

        homeAdapter = new HomeAdapter();
        listViewHome.setAdapter(homeAdapter);

        listViewHome.setOnItemClickListener(this);
        setTitle(name);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnCoursHome:
                intent = new Intent(this,CoursActivity.class);

                //intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                startActivity(intent);
                break;
            case R.id.btnCalendHome:
//                intent = new Intent(this,CalenderActivity.class);
//                startActivity(intent);
                //intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.btnMessHome:
                //intent = new Intent(this,MessageActivity.class);
                //startActivity(intent);
                // intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
            case R.id.btnTousHome:
                //intent = new Intent(this,AllActivity.class);
                //startActivity(intent);
                // intent.putExtra("****", "*****");//il faut etre remplit par les donnes relies
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DetailsActivity.class);
        String home = donne[position];
        intent.putExtra("home",home);
        startActivity(intent);
    }


    private class HomeAdapter extends BaseAdapter {
        LayoutInflater inflaterHome;
        public HomeAdapter() {

            inflaterHome= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return donne.length;
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
            View vHome = convertView;
            if(vHome==null){
                vHome=inflaterHome.inflate(android.R.layout.simple_list_item_1,parent,false);
            }

            TextView text = (TextView)vHome.findViewById(android.R.id.text1);
            text.setText(donne[position]);
            return vHome;
        }
    }
}