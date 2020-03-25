package com.example.user.solviolin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;

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

    }


}
