package com.example.user.solviolin.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.user.solviolin.BookedCourse;
import com.example.user.solviolin.extendedDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.UserListAdapter.AdminUserID;

public class DataParser4 extends AsyncTask<Void, Void, Integer>{ //For

    Context c;
    String jsonData;

    ProgressDialog pd;

    public static ArrayList<extendedDate> extendedDateList = new ArrayList<extendedDate>();

    extendedDate extendedDateClass;





    public DataParser4(Context c, String jsonData) {
        this.c = c;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing.... Please Wait");
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
           // Toast.makeText(c,"Unable to Parse", Toast.LENGTH_SHORT).show();
        }else
        {
           // Toast.makeText(c,"Parsed Successfully", Toast.LENGTH_SHORT).show();
            // Bind
        }
    }

    private int parseData(){
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo = null;




            for(int i = 0; i <ja.length();i++)
            {
                jo = ja.getJSONObject(i);
                String extendeduserID = jo.getString("userID");

                String userBranch = jo.getString("userBranch");
                String extendedCourseTeacher = jo.getString("courseTeacher");
                String extendedDate = jo.getString("extendedDate");
                String extendedendDate = jo.getString("endDate");

                extendedDateClass = new extendedDate(extendedDate, extendedendDate, extendeduserID, extendedCourseTeacher, userBranch);
                extendedDateList.add(extendedDateClass);









            }


            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  0;

    }
}
