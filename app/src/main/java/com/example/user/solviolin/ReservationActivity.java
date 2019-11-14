package com.example.user.solviolin;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.user.solviolin.mMySQL.Downloader1;
import com.example.user.solviolin.mMySQL.Downloader3;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;

public class ReservationActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        final Button monthReservationButton = (Button) findViewById(R.id.monthReservation);
        final Button dayReservationButton = (Button) findViewById(R.id.dayReservation);
        final LinearLayout base = (LinearLayout) findViewById(R.id.base);


        if(BookedList.size() <= 0)
        {
            dayReservationButton.setEnabled(false);
        }





        monthReservationButton.setOnClickListener(new View.OnClickListener() {
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
        });



    }


}
