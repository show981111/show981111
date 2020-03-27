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

public class acceptRegularTask extends AsyncTask<String, Void, String> {

    private Context context;
    private String pt_courseTeacher;
    private String pt_courseBranch;
    private String pt_userID;
    private String pt_startTime;
    private String pt_startDateAndDow;
    private String reject;
    waitListAdapter waitListAdapter;
    int position;
    ArrayList<waitlist_item> waitlist_items = new ArrayList<>();

    public acceptRegularTask(Context context, String pt_courseTeacher, String pt_courseBranch, String pt_userID, String pt_startTime, String pt_startDateAndDow, String reject, waitListAdapter waitListAdapter, int position, ArrayList<waitlist_item> waitlist_item) {
        this.context = context;
        this.pt_courseTeacher = pt_courseTeacher;
        this.pt_courseBranch = pt_courseBranch;
        this.pt_userID = pt_userID;
        this.pt_startTime = pt_startTime;
        this.pt_startDateAndDow = pt_startDateAndDow;
        this.reject = reject;
        this.waitListAdapter = waitListAdapter;
        this.position = position;
        this.waitlist_items = waitlist_item;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("pt_courseTeacher", pt_courseTeacher)
                .add("pt_courseBranch", pt_courseBranch)
                .add("pt_userID", pt_userID)
                .add("pt_startTime",pt_startTime)
                .add("pt_startDateAndDow", pt_startDateAndDow)
                .add("reject",reject)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("acceptwait", e.getMessage());
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("postWaitList",s);
        if(s.equals("success"))
        {
            waitlist_items.remove(position);
            waitListAdapter.notifyItemRemoved(position);
            waitListAdapter.notifyItemRangeChanged(position, waitlist_items.size());
            waitListAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(context,"성공적으로 요청되었습니다.",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }else if(s.equals("notEmpty")) {
            Toast toast = Toast.makeText(context, "해당 시간은 빈자리가 아닙니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if(s.substring(0,3).equals("int"))
        {
            Toast toast = Toast.makeText(context, "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if(s.equals("already"))
        {
            waitlist_items.remove(position);
            waitListAdapter.notifyItemRemoved(position);
            waitListAdapter.notifyItemRangeChanged(position, waitlist_items.size());
            waitListAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(context, "이미 예약되어있습니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if(s.equals("delete_success")){
            waitlist_items.remove(position);
            waitListAdapter.notifyItemRemoved(position);
            waitListAdapter.notifyItemRangeChanged(position, waitlist_items.size());
            waitListAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(context, "거절하였습니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }else
        {
            Toast toast = Toast.makeText(context, "실패하였습니다. 관리자에게 문의해주시기 바랍니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
