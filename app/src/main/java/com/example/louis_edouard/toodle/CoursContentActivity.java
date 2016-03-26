package com.example.louis_edouard.toodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class CoursContentActivity extends AppCompatActivity {
    TabLayout tabCoursContent;
    ViewPager pagerCoursContent;
    CoursContentAdaptor coursContentAdaptor;
    public final int nb = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_content);

        tabCoursContent=(TabLayout)findViewById(R.id.tabCoursContent);
        pagerCoursContent = (ViewPager)findViewById(R.id.pagerCoursContent);
        coursContentAdaptor = new CoursContentAdaptor(getSupportFragmentManager());
        pagerCoursContent.setAdapter(coursContentAdaptor);
        tabCoursContent.setupWithViewPager(pagerCoursContent);
        Intent intent = getIntent();
        String coursTitle =intent.getStringExtra("coursTitle");
        setTitle(coursTitle);
    }

    private class CoursContentAdaptor extends FragmentPagerAdapter{

        public CoursContentAdaptor(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            CoursFichFragment fich = new CoursFichFragment();
            CoursContenuFragment contenu = new CoursContenuFragment();
            CoursForumFragment forum = new CoursForumFragment();
            Bundle args = new Bundle();
            if(position==0) {
                return fich;
            }else if(position ==1){
                return contenu;
            }else if(position ==2){
                return  forum;
            }
            return null;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return nb;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String tabTitle="";
            if(position==0){
                tabTitle = "FICHE";
            }else if(position==1){
                tabTitle = "Contenu";
            }else if(position ==2){
                tabTitle = "Forum";
            }
            return tabTitle;
        }

    }
}
