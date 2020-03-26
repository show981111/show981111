package com.example.user.solviolin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class extendRequestTask extends AsyncTask<String, Void, String> {
    private String userID, extendTeacher, extendBranch, extendStartDate, extendEndDate, userDuration;
    private Context context;

    private ArrayList<BookedList> bookedLists;
    private BookedListAdapter bookedListAdapter;
    private int pos;

    public extendRequestTask(Context context ,String userID, String extendTeacher, String extendBranch, String extendStartDate , String extendEndDate,ArrayList<BookedList> bookedLists, BookedListAdapter bookedListAdapter, int pos) {
        this.userID = userID;
        this.extendTeacher = extendTeacher;
        this.extendBranch = extendBranch;
        this.extendStartDate = extendStartDate;
        this.extendEndDate = extendEndDate;
        this.context = context;
        this.bookedLists = bookedLists;
        this.bookedListAdapter = bookedListAdapter;
        this.pos = pos;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("userID",userID)
                .add("extendTeacher",extendTeacher)
                .add("extendBranch",extendBranch)
                .add("extendStartDate",extendStartDate)
                .add("extendEndDate",extendEndDate)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();//get new End Date
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String message;
        if(s.substring(0,1).equals("2"))//새로운 끝나는 시간을 받아온다... 따라서 첫글자는 2갯지
        {
            bookedLists.get(pos).setBookedEndDate(s);
            Log.d("extendRequestTask", String.valueOf(bookedLists.size()));
            //Log.d("cancelRequestTask", userCredit);
            bookedListAdapter.notifyItemChanged(pos);
            bookedListAdapter.notifyDataSetChanged();
            message = "연장하였습니다.";
        }else if(s.equals("unavailable"))
        {
            message = "수업을 먼저 취소해 주세요.";
        }else if(s.equals("notEmpty"))
        {
            message ="빈 시간대가 아닙니다.";
        }else{
            message = "실패하였습니다.";
        }
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
