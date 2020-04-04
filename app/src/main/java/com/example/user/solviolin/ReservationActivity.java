package com.example.user.solviolin;

import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.example.user.solviolin.adapter.Vp_adapter;
import com.google.android.material.tabs.TabLayout;


public class ReservationActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Fragment[] arrFragments = new Fragment[2];

        arrFragments[0] = new MonthFragment();
        arrFragments[1] = new DayFragment();



        ViewPager viewPager =(ViewPager) findViewById(R.id.vp_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        Vp_adapter adapter = new Vp_adapter(getSupportFragmentManager(),arrFragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

    }


}
