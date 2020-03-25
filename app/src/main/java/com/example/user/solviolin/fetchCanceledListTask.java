package com.example.user.solviolin;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.solviolin.MainActivity.userCredit;


public class fetchCanceledListTask extends AsyncTask<String , Void, BookedList[]> {

    private Context context;
    private String userID;
    private String cancelTeacher;
    private String cancelBranch;
    private String startDate;
    private String endDate;
    private int pos;
    private String option;

    private BookedListAdapter bookedListAdapter;
    private ArrayList<BookedList> bookedListArrayList;

    public fetchCanceledListTask(Context context, int pos,String userID, String cancelTeacher, String cancelBranch, String startDate, String endDate, BookedListAdapter bookedListAdapter, ArrayList<BookedList> bookedListArrayList, String option ) {
        this.context = context;
        this.pos = pos;
        this.userID = userID;
        this.cancelTeacher = cancelTeacher;
        this.cancelBranch = cancelBranch;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookedListAdapter = bookedListAdapter;
        this.bookedListArrayList = bookedListArrayList;
        this.option = option;
    }

    @Override
    protected BookedList[] doInBackground(String... strings) {

        String url = strings[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userID",userID)
                .add("option",option)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("fetchBookedList", "res");
            Gson gson = new Gson();
            BookedList[] bookedLists = gson.fromJson(response.body().charStream(), BookedList[].class);//이번학기 취소한 리스트를 받아온다.
            return bookedLists;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fetchBookedList", e.getMessage());
            return null;
        }

    }

    @Override
    protected void onPostExecute(BookedList[] fetchedBookedLists) {

        //send cancel request
        if(option.equals("cancel_cur")) {
            cancelRequestTask cancelRequestTask = new cancelRequestTask();
            cancelRequestTask.execute("http://show981111.cafe24.com/cancelCourse.php");
        }


//        for(BookedList bookedListitem : bookedLists)
//        {
//            canceledList.add(bookedListitem);
//        }

        super.onPostExecute(fetchedBookedLists);
    }

    class cancelRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("userID",userID)
                    .add("cancelTeacher",cancelTeacher)
                    .add("cancelBranch",cancelBranch)
                    .add("startDate",startDate)
                    .add("endDate",endDate)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d("cancelRequestTask", "res");

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("cancelRequestTask", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("success"))
            {
                bookedListArrayList.remove(pos);
                Log.d("cancelRequestTask", String.valueOf(bookedListArrayList.size()));
                //Log.d("cancelRequestTask", userCredit);
                bookedListAdapter.notifyItemRemoved(pos);
                bookedListAdapter.notifyItemRangeChanged(pos,bookedListArrayList.size());
                bookedListAdapter.notifyDataSetChanged();
                Toast.makeText(context,"성공적으로 취소하였습니다.",Toast.LENGTH_SHORT).show();
            }else if(s.equals("already"))
            {
                Toast.makeText(context,"이미 취소되어 있습니다.",Toast.LENGTH_SHORT).show();
            }else if(s.equals("creditOver")){
                Toast.makeText(context,"변경 가능 횟수를 초과하였습니다.",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
