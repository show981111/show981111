package com.example.user.solviolin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.solviolin.mMySQL.Downloader1;
import com.example.user.solviolin.mMySQL.Downloader3;

import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.curcount;
import static com.example.user.solviolin.MainActivity.delaycredit;
import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;//예약한 리스트 임포트하는 부분
//import static com.example.user.solviolin.LoginActivity.BookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.DayBookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedListcur;


public class Result extends AppCompatActivity {

    private ListView resultlist;
    private ListView dayresultlist;
    private BookedCourseListAdapter adapter;
    private DayBookingCourseListAdapter adapter1;
    private TextView delayCreditView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        new Downloader3(Result.this, "http://show981111.cafe24.com/DayBookedList.php").execute();
        String url = "";
        // new Downloader1(MainActivity.this, "http://show981111.cafe24.com/BookedCourseList.php").execute();
        switch (userBranch) {
            case "잠실":
                url = "http://show981111.cafe24.com/BookedCourseList_J.php";
                break;
            case "여의도":
                url = "http://show981111.cafe24.com/BookedCourseList_Y.php";
                break;
            case "시청":
                url = "http://show981111.cafe24.com/BookedCourseList_S.php";
                break;
            case "교대":
                url = "http://show981111.cafe24.com/BookedCourseList_K.php";
                break;
            case "광화문":
                url = "http://show981111.cafe24.com/BookedCourseList_G.php";
                break;
        }

        new Downloader1(Result.this, url).execute();
        //Toast.makeText(getApplicationContext(), "취소하였습니다." + userID + userName , Toast.LENGTH_LONG).show();



        resultlist = (ListView) findViewById(R.id.BookedCourseList);
        adapter = new BookedCourseListAdapter(getApplicationContext(),BookedList, this);
        adapter.notifyDataSetChanged();
        resultlist.setAdapter(adapter);

        delayCreditView = (TextView) findViewById(R.id.delayCreditView);
        /*int count = 0;//이번달 보강 잡은거
        delaycredit = 0;//이월된 보강 갯수
        for(int i = 0; i < personalDayBookedList.size(); i++)
        {
            if(personalDayBookedList.get(i).getNewlyBookedDate().equals("다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)") && (personalDayBookedList.get(i).getDataStatus().equals("going") || personalDayBookedList.get(i).getDataStatus().equals("cur") ))
            {
                delaycredit = delaycredit + 1;

            }
            if(personalDayBookedList.get(i).getCanceledCourseDate().substring(0,1).equals("[") && personalDayBookedList.get(i).getNewlyBookedDate().equals("다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)") && (personalDayBookedList.get(i).getDataStatus().equals("going")|| personalDayBookedList.get(i).getDataStatus().equals("cur")))
            {
                delaycredit = delaycredit + 1;

            }
            if( personalDayBookedList.get(i).getDataStatus().equals("cur"))
            {
                curcount = curcount + 1;
            }
        }*/

        delayCreditView.setText("이월된 보강 "+Integer.toString(delaycredit) + "회" + "\n" + "이번달 레슨 변경 가능한 횟수 "+ Integer.toString(2 - curcount));



        dayresultlist = (ListView) findViewById(R.id.ChangedCourseList);
        adapter1= new DayBookingCourseListAdapter(getApplicationContext(), personalDayBookedList, this);

        if(userName.equals("admin"))
        {
            adapter1= new DayBookingCourseListAdapter(getApplicationContext(), personalDayBookedList, this);
        }
        adapter1.notifyDataSetChanged();
        dayresultlist.setAdapter(adapter1);

    }
}
