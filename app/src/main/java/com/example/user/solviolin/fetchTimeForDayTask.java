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

public class fetchTimeForDayTask extends AsyncTask<String, Void, AvailableTimeForMonth[]> {
    private static ArrayList<String> timeList = new ArrayList<>();

    private Context context;
    private GridView gridView;
    private Fragment parent;
    private String userID;
    private String courseBranch;
    private String courseTeacher;
    private String userDuration;
    private String selectedDate;
    private String canceledDate;
    //for Admin
    public fetchTimeForDayTask(Context context, Fragment parent,GridView gridView, String userID, String courseBranch, String courseTeacher, String userDuration, String selectedDate,String canceledDate) {
        this.context = context;
        this.parent = parent;
        this.gridView = gridView;
        this.userID = userID;
        this.courseBranch = courseBranch;
        this.courseTeacher = courseTeacher;
        this.userDuration = userDuration;
        this.selectedDate = selectedDate;
        this.canceledDate = canceledDate;
    }
    //For student
    public fetchTimeForDayTask(Context context, Fragment parent,GridView gridView, String userID, String userDuration, String selectedDate,String canceledDate) {
        this.context = context;
        this.parent = parent;
        this.gridView = gridView;
        this.userID = userID;
        this.userDuration = userDuration;
        this.selectedDate = selectedDate;
        this.courseBranch = "";
        this.courseTeacher = "";
        this.canceledDate = canceledDate;
    }

    @Override
    protected AvailableTimeForMonth[] doInBackground(String... strings) {
        String url = strings[0];

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("userID", userID)
                .add("courseBranch", courseBranch)
                .add("courseTeacher", courseTeacher)
                .add("userDuration",userDuration)
                .add("selectedDate", selectedDate)
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
            Log.d("fetchTimeForDayTask", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(AvailableTimeForMonth[] times) {
        timeList.clear();
        for(AvailableTimeForMonth time : times)
        {
            timeList.add(time.getRegular_Time());
        }
        ButtonGridAdapter buttonGridAdapter = new ButtonGridAdapter(context,timeList,parent,userID,userDuration,courseTeacher,courseBranch,selectedDate,canceledDate,"day");
        buttonGridAdapter.notifyDataSetChanged();
        gridView.setAdapter(buttonGridAdapter);

        super.onPostExecute(times);
    }
}
