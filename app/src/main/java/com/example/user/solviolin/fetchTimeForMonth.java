package com.example.user.solviolin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;


import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.solviolin.MainActivity.userName;

//fetch teacher list for teacherSpinner in monthFragment
public class fetchTimeForMonth extends AsyncTask<String, Void, AvailableTimeForMonth[]> {

    private static ArrayList<courseTimeLine> courseTimeLineArrayList = new ArrayList<>();
    private static ArrayList<String> timeList = new ArrayList<>();
    private GridView timeButtonGrid;
    private Context context;
    private Fragment parent;

    private String courseDay;
    private String courseTeacher;
    private String startDate;

    private String gv_userID;
    private String gv_userBranch;
    private String gv_userDuration;

    public fetchTimeForMonth(GridView gv, Context c, Fragment fragment, String courseDay, String courseTeacher, String startDate, String gv_userID, String gv_userBranch, String gv_userDuration)
    {
        this.timeButtonGrid = gv;
        this.context = c;
        this.parent = fragment;
        this.courseDay = courseDay;
        this.courseTeacher = courseTeacher;
        this.startDate = startDate;
        this.gv_userID = gv_userID;
        this.gv_userBranch = gv_userBranch;
        this.gv_userDuration = gv_userDuration;
    }

    public static ArrayList<courseTimeLine> getCourseTimeLineArrayList() {
        return courseTimeLineArrayList;
    }

    public static void setCourseTimeLineArrayList(ArrayList<courseTimeLine> courseTimeLineArrayList) {
        fetchTimeForMonth.courseTimeLineArrayList = courseTimeLineArrayList;
    }

    @Override
    protected AvailableTimeForMonth[] doInBackground(String... strings) {
        String url= strings[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userBranch", gv_userBranch)
                .add("courseDay", this.courseDay)
                .add("courseTeacher", this.courseTeacher)
                .add("startDate",this.startDate)
                .add("userDuration", gv_userDuration)
                .add("userName",userName)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            AvailableTimeForMonth[] times = gson.fromJson(response.body().charStream(), AvailableTimeForMonth[].class);
            return times;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fetchTimeForMonth", e.getMessage());
            return null;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(AvailableTimeForMonth[] times) {
        super.onPostExecute(times);
        timeList.clear();

        if(times != null) {
            for (AvailableTimeForMonth time : times) {
                timeList.add(time.getRegular_Time());
            }

            ButtonGridAdapter buttonGridAdapter = new ButtonGridAdapter(context, timeList, parent,courseTeacher,startDate,courseDay,gv_userID,gv_userBranch,gv_userDuration);
            buttonGridAdapter.notifyDataSetChanged();
            timeButtonGrid.setAdapter(buttonGridAdapter);
        }else
        {
            Log.d("fetchTimeForMonth","NULL");
        }

    }

}
