package com.example.user.solviolin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.SAXParser;

import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userDuration;

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

    public fetchTimeForMonth(GridView gv, Context c, Fragment fragment, String courseDay, String courseTeacher, String startDate)
    {
        this.timeButtonGrid = gv;
        this.context = c;
        this.parent = fragment;
        this.courseDay = courseDay;
        this.courseTeacher = courseTeacher;
        this.startDate = startDate;
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
                .add("userBranch", userBranch)
                .add("courseDay", this.courseDay)
                .add("courseTeacher", this.courseTeacher)
                .add("startDate",this.startDate)
                .add("userDuration", userDuration)
                .build();
        Log.d("userBranchInFetch",userBranch);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("FetchTimeresponse", "res");
            Gson gson = new Gson();
            AvailableTimeForMonth[] times = gson.fromJson(response.body().charStream(), AvailableTimeForMonth[].class);
            return times;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FetchUSer", e.getMessage());
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
        for(AvailableTimeForMonth time : times)
        {
            timeList.add(time.getRegular_Time());
            Log.d("fetchTime",time.getRegular_Time());
        }

        ButtonGridAdapter buttonGridAdapter = new ButtonGridAdapter(context,timeList, parent);
        buttonGridAdapter.notifyDataSetChanged();
        timeButtonGrid.setAdapter(buttonGridAdapter);


        /*ArrayAdapter teacheradapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,HashedTeacherList);
        teacheradapter.notifyDataSetChanged();
        teacherspinner.setAdapter(teacheradapter);*/
    }

}
