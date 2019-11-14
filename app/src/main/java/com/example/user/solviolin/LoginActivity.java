package com.example.user.solviolin;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.mMySQL.DataParser;
import com.example.user.solviolin.mMySQL.Downloader1;
import com.example.user.solviolin.mMySQL.Downloader3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.UserListAdapter.AdminUserID;

public class LoginActivity extends AppCompatActivity {

    public static String InputURL;
    final static String urlAddress_J = "http://show981111.cafe24.com/CourseList_J.php";
    final static String urlAddress_Y = "http://show981111.cafe24.com/CourseList_Y.php";
    final static String urlAddress_S = "http://show981111.cafe24.com/CourseList_S.php";
    final static String urlAddress_K = "http://show981111.cafe24.com/CourseList_K.php";
    final static String urlAddress_G = "http://show981111.cafe24.com/CourseList_G.php";
    public static ArrayList<BookedCourse> BookedList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final TextView registerTextView = (TextView) findViewById(R.id.registerTextView);
        final Button loginButton = (Button) findViewById(R.id.loginButton);


        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                String userID = jsonResponse.getString("userID");
                                String userPassword = jsonResponse.getString("userPassword");
                                String userBranch = jsonResponse.getString("userBranch");
                                String userName = jsonResponse.getString("userName");
                                String userDuration = jsonResponse.getString("userDuration");
                                int userCredit = jsonResponse.getInt("userCredit");
                                if(!userDuration.equals("45")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPassword", userPassword);
                                    intent.putExtra("userBranch", userBranch);
                                    intent.putExtra("userCredit", userCredit);
                                    intent.putExtra("userDuration", userDuration);
                                    LoginActivity.this.startActivity(intent);
                                    //new Downloader1(LoginActivity.this, "http://show981111.cafe24.com/BookedCourseList.php").execute();
                                    //new Downloader3(LoginActivity.this, "http://show981111.cafe24.com/DayBookedList.php").execute();
                                }else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("45분 수업을 받는 학생은 웹사이트를 이용해주세요.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                               // Intent intent1 = new Intent(LoginActivity.this, MonthFragment.class);
                                //intent1.putExtra("userID", userID);
                               // intent1.putExtra("userBranch", userBranch);

                                //new Downloader1(LoginActivity.this, "http://show981111.cafe24.com/BookedCourseList.php").execute();


                                switch (userBranch){
                                    case "잠실":
                                        InputURL = urlAddress_J;
                                        break;
                                    case "여의도":
                                        InputURL = urlAddress_Y;
                                        break;
                                    case "시청":
                                        InputURL = urlAddress_S;
                                        break;
                                    case "교대":
                                        InputURL = urlAddress_K;
                                        break;
                                    case "광화문":
                                        InputURL = urlAddress_G;
                                        break;

                                }


                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }

                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);


               /*Response.Listener<JSONObject> responseListener1 = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonResponse = response.getJSONArray("response");
                            //boolean success = jsonResponse.getBoolean("success");
                            //JSONArray cast = jsonResponse.getJSONArray(response);

                            if (success) {
                                BookedList.clear();
                                BookedCourse bookedCourse = null;

                                String bookedUserID = jsonResponse.getString("userID");
                                int id = jsonResponse.getInt("courseID");
                                String courseBranch = jsonResponse.getString("courseBranch");
                                String bookedCourseTeacher = jsonResponse.getString("courseTeacher");
                                String bookedCourseDay = jsonResponse.getString("courseDay");
                                String bookedCourseTime = jsonResponse.getString("courseTime");
                                bookedCourse = new BookedCourse(bookedUserID, bookedCourseTeacher, bookedCourseDay, bookedCourseTime, id, courseBranch);
                                BookedList.add(bookedCourse);


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("데이터 로딩에 실패하였습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }



                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                };*/
                /*Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                BookedList.clear();
                                BookedCourse bookedCourse = null;

                                String bookedUserID = jsonObject.getString("userID");
                                int id = jsonObject.getInt("courseID");
                                String courseBranch = jsonObject.getString("courseBranch");
                                String bookedCourseTeacher = jsonObject.getString("courseTeacher");
                                String bookedCourseDay = jsonObject.getString("courseDay");
                                String bookedCourseTime = jsonObject.getString("courseTime");
                                bookedCourse = new BookedCourse(bookedUserID, bookedCourseTeacher, bookedCourseDay, bookedCourseTime, id, courseBranch);
                                BookedList.add(bookedCourse);
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                BookedListRequest bookedListRequest = new BookedListRequest(userID, responseListener1);
                RequestQueue queue1 = Volley.newRequestQueue(LoginActivity.this);
                queue1.add(bookedListRequest);*/


            }
        });




    }
}
