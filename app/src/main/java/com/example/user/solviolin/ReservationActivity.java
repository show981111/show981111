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

        //final Button monthReservationButton = (Button) findViewById(R.id.monthReservation);
        //final Button dayReservationButton = (Button) findViewById(R.id.dayReservation);
        //final LinearLayout base = (LinearLayout) findViewById(R.id.base);


        /*if(BookedList.size() <= 0)
        {
            dayReservationButton.setEnabled(false);
        }*/





        /*monthReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base.setVisibility(View.GONE);
                monthReservationButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                dayReservationButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new MonthFragment());
                fragmentTransaction.commit();
            }
        });

        dayReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base.setVisibility(View.GONE);
                //new Downloader3(ReservationActivity.this, "http://show981111.cafe24.com/DayBookedList.php").execute();
                monthReservationButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                dayReservationButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new DayFragment());
                fragmentTransaction.commit();
            }
        });*/



    }


}
