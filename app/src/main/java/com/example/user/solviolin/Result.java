package com.example.user.solviolin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;


public class Result extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Fragment[] arrFragments = new Fragment[3];

        arrFragments[0] = new pastResult();
        arrFragments[1] = new curResult();
        arrFragments[2] = new changeListFragment();



        ViewPager viewPager =(ViewPager) findViewById(R.id.vp_pager_result);
        TabLayout tabLayout = findViewById(R.id.tab_layout_result);
        tabLayout.setupWithViewPager(viewPager);

        Vp_resAdapter adapter = new Vp_resAdapter(getSupportFragmentManager(),arrFragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }
}
