package com.example.user.solviolin.getData;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.solviolin.Data.BookedList;
import com.example.user.solviolin.adapter.BookedListAdapter;
import com.example.user.solviolin.ReservationActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.solviolin.MainActivity.userName;


public class fetchCanceledListTask extends AsyncTask<String , Void, BookedList[]> {

    private Context context;
    private String userID;
    private String cancelTeacher;
    private String cancelBranch;
    private String startDate;
    private String endDate;
    private int pos;
    private String option;

    public String getCanceledDate() {
        return canceledDate;
    }

    private String canceledDate;

    private BookedListAdapter bookedListAdapter;
    private ArrayList<BookedList> bookedListArrayList;

    private TextView tv_cancelTeacher;
    private TextView tv_cancelBranch;
    private TextView tv_cancelDate;
    private Button choosenewDate;

    public fetchCanceledListTask(Context context, String userID ,TextView tv_cancelTeacher, TextView tv_cancelBranch, TextView tv_cancelDate, String option,Button choosenewDate) {
        this.context = context;
        this.userID = userID;
        this.tv_cancelTeacher = tv_cancelTeacher;
        this.tv_cancelBranch = tv_cancelBranch;
        this.tv_cancelDate = tv_cancelDate;
        this.option = option;
        this.choosenewDate = choosenewDate;
    }

    public fetchCanceledListTask(Context context, int pos, String userID, String cancelTeacher, String cancelBranch, String startDate, String endDate, BookedListAdapter bookedListAdapter, ArrayList<BookedList> bookedListArrayList, String option ) {
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
        if(userID == null || option == null) return null;

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
        super.onPostExecute(fetchedBookedLists);
        //send cancel request
        if(fetchedBookedLists != null) {
            if (option.equals("cancel_cur")) {
                cancelRequestTask cancelRequestTask = new cancelRequestTask();
                cancelRequestTask.execute("http://show981111.cafe24.com/cancelCourse.php");//왜 굳이 파싱을 하고 하지? 의미가 있냐?
            } else if (option.equals("cancelAll")) {
                bookedListArrayList = new ArrayList<>();
                bookedListArrayList.clear();
                for (BookedList canceledCourse : fetchedBookedLists) {
                    bookedListArrayList.add(canceledCourse);//이번학기 지난학기 취소했지만 아직 보강을 안잡은 수업을 파싱한다.
                }
                Log.d("fetchCanceledListTask", String.valueOf(bookedListArrayList.size()));
                if (bookedListArrayList.size() > 0) {
                    choosenewDate.setEnabled(true);
                    tv_cancelTeacher.setText(bookedListArrayList.get(bookedListArrayList.size() - 1).getBookedTeacher());
                    tv_cancelBranch.setText(bookedListArrayList.get(bookedListArrayList.size() - 1).getBookedBranch());
                    tv_cancelDate.setText(bookedListArrayList.get(bookedListArrayList.size() - 1).getBookedStartDate());
                    canceledDate = bookedListArrayList.get(bookedListArrayList.size() - 1).getBookedStartDate();
                    Log.d("fetchCanceledListTask", canceledDate);
                } else {
                    if (!userName.equals("admin")) {
                        choosenewDate.setEnabled(false);
                        Toast toast = Toast.makeText(context, "먼저 수업을 취소해주세요.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        canceledDate = "admin";
                    }
                }

            }
        }


//        for(BookedList bookedListitem : bookedLists)
//        {
//            canceledList.add(bookedListitem);
//        }
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
                    .add("userName",userName)
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
                //Toast.makeText(context,"성공적으로 취소하였습니다.",Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog = builder.setMessage("성공적으로 취소하였습니다. 보강을 예약하러 가시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goToReservation =new Intent(context, ReservationActivity.class);
                                context.startActivity(goToReservation);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
            }else if(s.equals("already"))
            {
                Toast.makeText(context,"이미 취소되어 있습니다.",Toast.LENGTH_SHORT).show();
            }else if(s.equals("creditOver")){
                Toast.makeText(context,"변경 가능 횟수를 초과하였습니다.",Toast.LENGTH_SHORT).show();
            }else if(s.equals("timeout"))
            {
                Toast.makeText(context,"수업 4시간 전까지만 취소가 가능합니다",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"실패하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
