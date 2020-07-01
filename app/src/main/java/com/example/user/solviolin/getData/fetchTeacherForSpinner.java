package com.example.user.solviolin.getData;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.solviolin.Data.courseTimeLine;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//fetch teacher list for teacherSpinner in monthFragment
public class fetchTeacherForSpinner extends AsyncTask<String, Void, courseTimeLine[]> {

    private static ArrayList<courseTimeLine> courseTimeLineArrayList = new ArrayList<>();
    private static ArrayList<String> TeacherList = new ArrayList<>();
    private Spinner teacherspinner;
    private Context context;
    private String fetch_userBranch;

    public fetchTeacherForSpinner(Spinner sp, Context c, String fetch_userBranch)
    {
        this.teacherspinner = sp;
        this.context = c;
        this.fetch_userBranch = fetch_userBranch;
    }

    public static ArrayList<courseTimeLine> getCourseTimeLineArrayList() {
        return courseTimeLineArrayList;
    }

    public static void setCourseTimeLineArrayList(ArrayList<courseTimeLine> courseTimeLineArrayList) {
        fetchTeacherForSpinner.courseTimeLineArrayList = courseTimeLineArrayList;
    }

    @Override
    protected courseTimeLine[] doInBackground(String... strings) {
        String url= strings[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userBranch", fetch_userBranch)
                .build();
        Log.d("userBranchInFetch",fetch_userBranch);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("FetchTry", "try");
            Gson gson = new Gson();
            courseTimeLine[] courseTimeLines = gson.fromJson(response.body().charStream(), courseTimeLine[].class);
            return courseTimeLines;

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
    protected void onPostExecute(courseTimeLine[] courseTimeLines) {
        super.onPostExecute(courseTimeLines);
        courseTimeLineArrayList.clear();
        TeacherList.clear();
        for(courseTimeLine courseTimeLine : courseTimeLines)
        {
            if(courseTimeLine != null) {
                courseTimeLineArrayList.add(courseTimeLine);
                Log.d("fetchTeacher", String.valueOf(courseTimeLine.getCourseTeacher()));
            }
        }

        setCourseTimeLineArrayList(courseTimeLineArrayList);

        for(int i = 0; i < courseTimeLineArrayList.size(); i++)
        {
            TeacherList.add(courseTimeLineArrayList.get(i).getCourseTeacher());
            Log.d("TeacherList",courseTimeLineArrayList.get(i).getCourseTeacher());
        }

        HashSet<String> hashSet = new HashSet<String>(TeacherList);
        ArrayList<String> HashedTeacherList = new ArrayList<String>(hashSet);

        ArrayAdapter teacheradapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,HashedTeacherList);
        teacheradapter.notifyDataSetChanged();
        teacherspinner.setAdapter(teacheradapter);
    }

}
