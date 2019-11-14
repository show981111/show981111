package com.example.user.solviolin.mMySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.user.solviolin.Exclusion;
import com.example.user.solviolin.Open;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser6 extends AsyncTask<Void, Void, Integer>{ //For

    Context c;
    String jsonData;

    ProgressDialog pd;

    public static ArrayList<Open> openList = new ArrayList<Open>();

    Open openDateClass;





    public DataParser6(Context c, String jsonData) {
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




            for(int i = 0; i <ja.length();i++)
            {
                jo = ja.getJSONObject(i);


                String openBranch = jo.getString("courseBranch");
                String openTeacher = jo.getString("courseTeacher");
                String openStartDate = jo.getString("startDate");
                String openEndDate = jo.getString("endDate");

                openDateClass = new Open(openStartDate, openEndDate, openBranch, openTeacher);
                openList.add(openDateClass);









            }


            return 1;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  0;

    }
}
