package com.example.user.solviolin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.mMySQL.Downloader1;
import com.example.user.solviolin.mMySQL.Downloader3;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        Button reservationButton = (Button) findViewById(R.id.reservationButton);
        Button resultButton = (Button) findViewById(R.id.resultButton);
        Button initializeButton = (Button) findViewById(R.id.initializeButton);
        Button linkButton = (Button)findViewById(R.id.gotohompageButton);
        Button termExtendButton = (Button)findViewById(R.id.bt_termExtend);
        Button bt_waitList = findViewById(R.id.bt_waitList);


        termExtendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder ad_termExtend = new AlertDialog.Builder(MainActivity.this);

                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText et_Start = new EditText(MainActivity.this);
                et_Start.setHint("시작날짜(YYYY-MM-DD)");
                layout.addView(et_Start);

                final EditText et_End = new EditText(MainActivity.this);
                et_End.setHint("끝나는 날짜(YYYY-MM-DD)");
                layout.addView(et_End);

                ad_termExtend.setView(layout).setTitle("학기 연장").setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String extendStart = et_Start.getText().toString();
                        String extendEnd = et_End.getText().toString();

                        extendTask extendTask = new extendTask(extendStart, extendEnd, MainActivity.this);
                        extendTask.execute("http://show981111.cafe24.com/putExtendTerm.php");

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "취소하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
                ad_termExtend.show();
            }
        });

        bt_waitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent waitListIntent = new Intent(MainActivity.this, waitListActivity.class);
                MainActivity.this.startActivity(waitListIntent);
            }
        });

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
            initializeButton.setVisibility(View.GONE);
            termExtendButton.setVisibility(View.GONE);
            bt_waitList.setVisibility(View.GONE);
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


    }

    class extendTask extends AsyncTask<String, Void, String>
    {
        private String termStart;
        private String termEnd;
        private Context c;

        public extendTask(String termStart, String termEnd, Context c) {
            this.termStart = termStart;
            this.termEnd = termEnd;
            this.c = c;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url= strings[0];

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("termStart", termStart)
                    .add("termEnd", termEnd)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                //Log.d("response", response.body().string());

                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("response",s);
            if(s.equals("success"))
            {
                Log.d("response",s);
                Toast.makeText(c, "완료되었습니다",Toast.LENGTH_SHORT).show();
            }else if(s.equals("notMatched"))
            {
                Log.d("response",s);
                Toast.makeText(c, "형식을 확인해주세요!",Toast.LENGTH_SHORT).show();
            }else if(s.equals("redunt"))
            {
                Log.d("response",s);
                Toast.makeText(c, "이미 연장된 날짜입니다.",Toast.LENGTH_SHORT).show();
            }else
            {
                Log.d("response",s);
                Toast.makeText(c, "날짜를 다시 확인해주세요",Toast.LENGTH_SHORT).show();
            }
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
