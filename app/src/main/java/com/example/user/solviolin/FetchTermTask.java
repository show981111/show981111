package com.example.user.solviolin;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.solviolin.termList;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.user.solviolin.MainActivity.userName;

class FetchTermTask extends AsyncTask<String, Void, termList[]>
{
    private ArrayList<termList> termListsArray = new ArrayList<>();
    private  DatePickerDialog datePickerDialog;


    public FetchTermTask(DatePickerDialog datePickerDialog) {
        this.datePickerDialog = datePickerDialog;
    }

    @Override
    protected termList[] doInBackground(String... strings) {
        String url = strings[0];

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            termList[] termLists = gson.fromJson(response.body().charStream(), termList[].class);
            return termLists;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("termListFetch", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(termList[] termLists) {
        super.onPostExecute(termLists);

        for(termList termitem : termLists)
        {
            termListsArray.add(termitem);
        }

        String maxDate = "";
        for(int i = 0; i < termListsArray.size(); i++) //asec order
        {
            if(!userName.equals("admin")) {
                if(i == 1) {
                    maxDate = termListsArray.get(i).getTermEnd();
                }
            }else
            {
                if(i == 2) {
                    maxDate = termListsArray.get(i).getTermEnd();
                }
            }
        }

        SimpleDateFormat datetimeFormat_Date = new SimpleDateFormat("yyyy-MM-dd");

        String today = datetimeFormat_Date.format(new Date());

        Calendar minCal = Calendar.getInstance();
        Calendar maxCal = Calendar.getInstance();

        if(today.equals("") || maxDate.equals(""))
        {

        }else
        {
            try {
                minCal.setTime(datetimeFormat_Date.parse(today));
                maxCal.setTime(datetimeFormat_Date.parse(maxDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(maxCal.getTimeInMillis());
        }
    }
}