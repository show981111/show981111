package com.example.user.solviolin.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.user.solviolin.BookedCourse;
import com.example.user.solviolin.DayBookingCourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.UserListAdapter.AdminUserID;

public class DataParser3 extends AsyncTask<Void, Void, Integer>{ //For

    Context c;
    String jsonData;

    ProgressDialog pd;

    public static ArrayList<DayBookingCourse> DayBookedList = new ArrayList<>();
    DayBookingCourse dayBookingCourse;
    public static ArrayList<DayBookingCourse> personalDayBookedList = new ArrayList<>();
    public static ArrayList<DayBookingCourse> personalDayBookedListcur = new ArrayList<>();
    DayBookingCourse personaldayBookingCourse;
    DayBookingCourse personaldayBookingCoursecur;


    public static int reservationInThisMonth = 0;
    public static int amountofGoing = 0;




    public DataParser3(Context c, String jsonData) {
        this.c = c;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("데이터를 불러오는중(3)");
        pd.setMessage("잠시만 기다려주세요.");
        pd.show();
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
            //Toast.makeText(c,"Unable to Parse", Toast.LENGTH_SHORT).show();
        }else
        {
            //Toast.makeText(c,"Parsed Successfully", Toast.LENGTH_SHORT).show();
            // Bind
        }
    }

    private int parseData(){
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo = null;

            dayBookingCourse = null;
            personaldayBookingCourse = null;
            personaldayBookingCoursecur = null;
            DayBookedList.clear();
            personalDayBookedList.clear();
            personalDayBookedListcur.clear();
            for(int i = 0; i <ja.length();i++)
            {
                jo = ja.getJSONObject(i);
                String DayBookingTeacher = jo.getString("courseTeacher");
                String canceledCourseDate = jo.getString("canceledCourseDate");
                String newlyBookedDate = jo.getString("newlyBookedDate");
                String bookedUserID = jo.getString("userID");
                String bookedUserName = jo.getString("userName");
                String bookedCourseBranch = jo.getString("userBranch");
                String endDate = jo.getString("endDate");
                String dataStatus = jo.getString("dataStatus");


                dayBookingCourse = new DayBookingCourse(canceledCourseDate, newlyBookedDate, bookedUserID, bookedUserName, DayBookingTeacher, bookedCourseBranch, endDate,dataStatus );
                DayBookedList.add(dayBookingCourse); /*else if(userID.equals("administer"))
               {
                   if(bookinguserID.equals(AdminUserID))
                   {
                       bookedCourse = new BookedCourse(bookinguserID, bookedCourseTeacher, bookedCourseDay, bookedCourseTime, id, courseBranch);
                       BookedList.add(bookedCourse);
                   }
               }*/
                if(bookedUserID.equals(userID) )
                {
                    personaldayBookingCourse = new DayBookingCourse(canceledCourseDate, newlyBookedDate, bookedUserID, bookedUserName, DayBookingTeacher, bookedCourseBranch, endDate, dataStatus );
                    personalDayBookedList.add(personaldayBookingCourse);
                    if(dataStatus.equals("cur")) {
                        personaldayBookingCoursecur = new DayBookingCourse(canceledCourseDate, newlyBookedDate, bookedUserID, bookedUserName, DayBookingTeacher, bookedCourseBranch, endDate, dataStatus);
                        personalDayBookedListcur.add(personaldayBookingCoursecur);
                    }
                }else if(userName.equals("admin"))
                {
                    if(bookedUserID.equals(AdminUserID))
                    {
                        personaldayBookingCourse = new DayBookingCourse(canceledCourseDate, newlyBookedDate, bookedUserID, bookedUserName, DayBookingTeacher, bookedCourseBranch, endDate,dataStatus );
                        personalDayBookedList.add(personaldayBookingCourse);
                    }
                }


            }


            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  0;

    }
}
