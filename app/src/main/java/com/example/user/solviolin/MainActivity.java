package com.example.user.solviolin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.mMySQL.Downloader;
import com.example.user.solviolin.mMySQL.Downloader1;
import com.example.user.solviolin.mMySQL.Downloader2;
import com.example.user.solviolin.mMySQL.Downloader3;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedList;

public class MainActivity extends AppCompatActivity {

    public static String userID;
    public static int userCredit; // 크레딧 조절을 위해 조절되는 값
    public static String userBranch;
    public static String userName;
    public static String userDuration;
    public static int delaycredit = 0;
    public static int curcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //new Downloader3(MainActivity.this, "http://show981111.cafe24.com/DayBookedList.php").execute();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userBranch = intent.getStringExtra("userBranch");
        userName = intent.getStringExtra("userName");
        userDuration = intent.getStringExtra("userDuration");
        userCredit = intent.getIntExtra("userCredit", 2);
        String userPassword = intent.getStringExtra("userPassword");
        //Toast.makeText(getApplicationContext(),"got it!"+userID, Toast.LENGTH_SHORT).show();
        String url = "";

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

        new Downloader1(MainActivity.this, url).execute(); //get booked list by oldversion


        Downloader3 download = new Downloader3(MainActivity.this, "http://show981111.cafe24.com/DayBookedList.php");
        download.execute();
        /*delaycredit = 0;//이월된 보강 갯수
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
        Button managementButton = (Button) findViewById(R.id.managementButton);
        Button reservationButton = (Button) findViewById(R.id.reservationButton);
        Button resultButton = (Button) findViewById(R.id.resultButton);
        Button initializeButton = (Button) findViewById(R.id.initializeButton);
        Button linkButton = (Button)findViewById(R.id.gotohompageButton);

        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";

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

                new Downloader1(MainActivity.this, url).execute(); //get booked list by oldversion
                delaycredit = 0;//이월된 보강 갯수
                curcount = 0;
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
                }
                Intent reservationIntent = new Intent(MainActivity.this, ReservationActivity.class);
                MainActivity.this.startActivity(reservationIntent);
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
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
                new Downloader1(MainActivity.this, url).execute();
                delaycredit = 0;//이월된 보강 갯수
                curcount = 0;
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
                }
                Downloader3 download = new Downloader3(MainActivity.this, "http://show981111.cafe24.com/DayBookedList.php");
                download.execute();




                Intent resultIntent = new Intent(MainActivity.this, Result.class);
                MainActivity.this.startActivity(resultIntent);
            }
        });

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.solviolin.com"));
                startActivity(myIntent);
            }
        });









        if(!userName.equals("admin"))
        {
            managementButton.setVisibility(View.GONE);
            initializeButton.setVisibility(View.GONE);

        }

        initializeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog dialog = builder.setMessage("정말 초기화 하시겟습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                Toast.makeText(MainActivity.this, "초기화 하였습니다.", Toast.LENGTH_LONG).show();



                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                AlertDialog dialog = builder.setMessage("초기화 하였습니다다.")
                                                        .setNegativeButton("확인", null)
                                                       .create();
                                                dialog.show();

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                initializeRequest initializeRequest = new initializeRequest(responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                queue.add(initializeRequest);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "취소하였습니다.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                dialog.show();


            }
        });

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
                String url = "";
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

                new Downloader1(MainActivity.this, url).execute();

                Downloader3 download = new Downloader3(MainActivity.this, "http://show981111.cafe24.com/DayBookedList.php");
                download.execute();

            }
        });

    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        protected void onPreExecute() {
            target = "http://show981111.cafe24.com/List.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{

                URL url = new URL(target);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e){
                e.printStackTrace();
            }
            return  null;
        }

        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        public void onPostExecute(String result){
            Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);
            MainActivity.this.startActivity(intent);
        }
    }

    private long lastTimeBackPressed;

    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 눌러 종료합니다", Toast.LENGTH_SHORT);
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
