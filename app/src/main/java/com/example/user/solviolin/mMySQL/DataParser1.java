package com.example.user.solviolin.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.solviolin.BookedCourse;
import com.example.user.solviolin.Course;
import com.example.user.solviolin.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.example.user.solviolin.MainActivity.userCredit;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.UserListAdapter.AdminUserID;

public class DataParser1 extends AsyncTask<Void, Void, Integer>{ //For

    Context c;
    String jsonData;

    ProgressDialog pd;
    public static ArrayList<Integer> bookedCourseIDList_J = new ArrayList<>();
    public static ArrayList<Integer> bookedCourseIDList_K = new ArrayList<>();
    public static ArrayList<Integer> bookedCourseIDList_Y = new ArrayList<>();
    public static ArrayList<Integer> bookedCourseIDList_S = new ArrayList<>();
    public static ArrayList<Integer> bookedCourseIDList_G = new ArrayList<>();

    public static ArrayList<BookedCourse> BookedList = new ArrayList<>();

    int size;
    String var;


    public DataParser1(Context c, String jsonData) {
        this.c = c;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("데이터를 불러오는중");
        pd.setMessage("잠시만 기다려주세요.");
       // pd.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        pd.dismiss();

        if(result == 0)
        {
            Toast.makeText(c,"ERROR", Toast.LENGTH_SHORT).show();
        }else
        {
            //Toast.makeText(c,"Parsed Successfully"+userID, Toast.LENGTH_SHORT).show();
            // Bind
        }
    }

    private int parseData(){
        try {
            JSONArray jaa = new JSONArray(jsonData);
            JSONObject joo = null;

            bookedCourseIDList_J.clear();
            bookedCourseIDList_K.clear();
            bookedCourseIDList_Y.clear();
            bookedCourseIDList_S.clear();
            bookedCourseIDList_G.clear();

            BookedList.clear();
            BookedCourse bookedCourse = null;
            long length = jaa.length();

            for(int k = 0; k < length;k++)
            {

                joo = jaa.getJSONObject(k);
                String bookedUserID = joo.getString("userID");
                int id = joo.getInt("courseID");
                String courseBranch = joo.getString("courseBranch");
                String bookedCourseTeacher = joo.getString("courseTeacher");
                String bookedCourseDay = joo.getString("courseDay");
                String bookedCourseTime = joo.getString("courseTime");
                String startDate = joo.getString("startDate");

               if(bookedUserID.equals(userID))//문자열 넣어줘도 판별 불가.
                {
                    size = 1;
                   bookedCourse = new BookedCourse(bookedUserID, bookedCourseTeacher, bookedCourseDay, bookedCourseTime, id, courseBranch, startDate);
                   BookedList.add(bookedCourse);
                }else if(userName.equals("admin"))
               {
                   if(bookedUserID.equals(AdminUserID))
                   {
                       bookedCourse = new BookedCourse(bookedUserID, bookedCourseTeacher, bookedCourseDay, bookedCourseTime, id, courseBranch, startDate);
                       BookedList.add(bookedCourse);
                   }
               }

               switch (courseBranch){ // 이미 예약된 코스 리스트를 받는다.....
                    case "잠실":
                        bookedCourseIDList_J.add(id);
                        break;
                    case "여의도":
                        bookedCourseIDList_Y.add(id);
                        break;
                    case "시청":
                        bookedCourseIDList_S.add(id);
                        break;
                    case "교대":
                        bookedCourseIDList_K.add(id);
                        break;
                    case "광화문":
                        bookedCourseIDList_G.add(id);
                        break;
                }
            }


            return 1;

        }catch (JSONException e){
            e.printStackTrace();

        }

        return  0;

    }
}
