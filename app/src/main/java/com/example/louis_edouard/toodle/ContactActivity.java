package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private View header;
    TextView drawer_txt_name;
    TextView drawer_txt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Contacts");
        header = (View)navigationView.getHeaderView(0);
        drawer_txt_name = (TextView)header.findViewById(R.id.drawer_txt_name);
        drawer_txt_name.setText(HomeDrawerActivity.userName);
        drawer_txt_email = (TextView)header.findViewById(R.id.drawer_txt_email);
        //TODO: retrieve user's email address
        drawer_txt_email.setText(HomeDrawerActivity.userName + "@email.com");
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
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
            intent = new Intent(this,MessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            onBackPressed();
        } else if (id == R.id.nav_send) {
            intent = new Intent(this, SendMessageActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
