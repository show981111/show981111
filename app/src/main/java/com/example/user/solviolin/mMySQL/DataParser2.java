package com.example.user.solviolin.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.solviolin.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.user.solviolin.mMySQL.DataParser.courseBranch;
import static com.example.user.solviolin.mMySQL.DataParser.courseDay;
import static com.example.user.solviolin.mMySQL.DataParser.courseID;
import static com.example.user.solviolin.mMySQL.DataParser.courseTeacher;
import static com.example.user.solviolin.mMySQL.DataParser.courseTime;

public class DataParser2 extends AsyncTask<Void, Void, Integer>{

    Context c;
    String jsonData;

    ProgressDialog pd;
    Course course;

    final ArrayList<Integer> index = new ArrayList<>();




    public DataParser2(Context c, String jsonData) {
        this.c = c;


        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("데이터를 불러오는중(2)");
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
            Toast.makeText(c,"Unable to Parse", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(c,"Parsed Successfully", Toast.LENGTH_SHORT).show();
            // Bind


            //마지막 인자가 스피너에 들어가는 어레이리스트




        }
    }

    private int parseData(){
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo = null;

            courseTeacher.clear();
            courseDay.clear();
            courseTime.clear();



            for(int i = 0; i <ja.length();i++)
            {
                jo = ja.getJSONObject(i);
                int id = jo.getInt("courseID");
                String name = jo.getString("courseTeacher");
                String day = jo.getString("courseDay");
                String time = jo.getString("courseTime");
                String branch = jo.getString("courseBranch");



                courseID.add(id);
                courseTeacher.add(name);//그 어레이 리스트 바인딩
                courseDay.add(day);
                courseTime.add(time);
                courseBranch.add(branch);


            }



            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  0;

    }
}
