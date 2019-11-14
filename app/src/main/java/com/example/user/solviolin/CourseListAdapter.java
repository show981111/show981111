package com.example.user.solviolin;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class CourseListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;
    //private Fragment parent;

    public CourseListAdapter(Context context, List<Course> courseList ) { //Fragment parent 추가
        this.context = context;
        this.courseList = courseList;
        //this.parent = parent;
        // new BackgroundTask().execute();
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View v = View.inflate(context, R.layout.daylist, null);
        //TextView courseDay = (TextView)v.findViewById(R.id.courseDaySpinnerText); //xml 과 바인딩

        //courseDay.setText(courseList.get(position).getCourseDay());

       // v.setTag(courseList.get(position).getCourseID());
       // return v;
        return null;
    }

    // class BackgroundTask extends AsyncTask<Void, Void, String>
    //{
    //   String target;
    //   protected void onPreExecute() {
    //       try{
    //           target = ""+ URLEncoder.encode(courseTeacher, "UTF-8"); //해당 php 파일 , 첫번쨰 스피너에서 선택된 선생 이름
//
    //           }catch(Exception e)
    //       {
    //           e.printStackTrace();
    //       }
    //   }
//
    //       protected  String doInBackground(Void... voids){
    //       try{
    //           URL url = new URL(target);
    //          HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    //          InputStream inputStream = httpURLConnection.getInputStream();
    //          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    //          String temp;
    //          StringBuilder stringBuilder = new StringBuilder();
    //          while((temp = bufferedReader.readLine()) != null)
    //          {
    //              stringBuilder.append(temp + "\n");
    //          }
    //          bufferedReader.close();
    //          inputStream.close();
    //          httpURLConnection.disconnect();
    //          return stringBuilder.toString().trim();
//
//
    //          }catch(Exception e){
    //          e.printStackTrace();
    //      }
    //      return null;
    //  }
//
    //      public void onProgressUpdate(Void... values){
    //      super.onProgressUpdate();
    //  }
//
    //      public void onPostExecute(String result){
    //      try{
    //          JSONObject jsonObject = new JSONObject(result);
    //          JSONArray jsonArray = jsonObject.getJSONArray("response");
    //          int count = 0;
    //          String courseTeacher, courseDay, courseTime;
    //          int courseID;
    //          while(count< jsonArray.length())
    //          {
    //              JSONObject object = jsonArray.getJSONObject(count);
    //              courseID = object.getInt("courseID");
    //              courseTeacher = object.getString("courseTeacher");
    //              courseDay = object.getString("courseDay");
    //              courseTime = object.getString("courseTime");
    //            Course course = new Course(courseID, courseTeacher, courseDay, courseTime);//생성자를 통해 값 넣어주기
    //              courseList.add(course);
    //              count++;
    //            }
    //       }catch (Exception e ){
    //          e.printStackTrace();
    //      }
    //      }



    //}
}


