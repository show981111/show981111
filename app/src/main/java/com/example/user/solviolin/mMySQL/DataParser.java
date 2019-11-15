package com.example.user.solviolin.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.solviolin.Course;
import com.example.user.solviolin.MonthFragment;

import static com.example.user.solviolin.MainActivity.userBranch;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class DataParser extends AsyncTask<Void, Void, Integer>{

    Context c;
    Spinner sp;
    String jsonData;

    ProgressDialog pd;
    public static ArrayList<String> courseTeacher = new ArrayList<>();
    public static ArrayList<Integer> courseID = new ArrayList<>();
    public static ArrayList<String> courseDay = new ArrayList<>();
    public static ArrayList<String> courseTime = new ArrayList<>();
    public static ArrayList<String> courseBranch = new ArrayList<>();
    public static ArrayList<String> courseTeacherParse = new ArrayList<>();
    Course course;

    final ArrayList<Integer> index = new ArrayList<>();




    public DataParser(Context c, Spinner sp, String jsonData) {
        this.c = c;
        this.sp = sp;

        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("데이터를 불러오는중(0)");
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


            ArrayAdapter adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1,courseTeacherParse);

            //마지막 인자가 스피너에 들어가는 어레이리스트
            sp.setAdapter(adapter);




        }
    }

    private int parseData(){
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo = null;

            courseTeacher.clear();
            courseDay.clear();
            courseTime.clear();
            courseTeacherParse.clear();

            Course cs = null;
            for(int i = 0; i <ja.length();i++)
            {
                jo = ja.getJSONObject(i);
                int id = jo.getInt("courseID");
                String name = jo.getString("courseTeacher");
                String day = jo.getString("courseDay");
                String time = jo.getString("courseTime");
                String branch = jo.getString("courseBranch");

                course = new Course(id, name, day, time);

                courseID.add(id);
                courseTeacher.add(name);//그 어레이 리스트 바인딩
                courseDay.add(day);
                courseTime.add(time);
                courseBranch.add(branch);


                courseTeacherParse.add(name);

                HashSet hs = new HashSet();
                hs.addAll(courseTeacherParse);
                courseTeacherParse.clear();
                courseTeacherParse.addAll(hs);
            }



            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  0;

    }
}
