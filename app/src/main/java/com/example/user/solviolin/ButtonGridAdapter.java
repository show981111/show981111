package com.example.user.solviolin;


import android.content.Context;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userName;

public class ButtonGridAdapter extends BaseAdapter{
    Context context = null;
    ArrayList<String> buttonNames = null;
    private Fragment parent;

    private String courseTeacher;
    private String courseBranch;
    private String userID;
    private String startTime;
    private String userDuration;
    private String courseDay;
    private String startDate;


    private int IDindex;

    public ButtonGridAdapter(Context context, ArrayList<String> buttonNames, Fragment parent, String courseTeacher, String startDate, String courseDay) {
        this.context = context;
        this.buttonNames = buttonNames;
        this.parent = parent;
        this.courseTeacher = courseTeacher;
        this.startDate = startDate;
        this.courseDay = courseDay;
    }

    @Override
    public int getCount() {
        return (null != buttonNames) ? buttonNames.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return buttonNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearAdapter(){
        buttonNames.clear();
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        Button button = null;

        if(null != convertView){
            button = (Button)convertView;
        }
        else{
            button = new Button(context);
            button.setText(buttonNames.get(position));
        }

        final Button finalButton = button;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime = finalButton.getText().toString();
                if(userName.equals("admin"))
                {

                }else
                {   //if it is student, post to waitList!
                    userID = MainActivity.userID;
                    courseBranch = userBranch;
                    userDuration = MainActivity.userDuration;


                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    AlertDialog dialog = builder.setMessage(courseTeacher + "선생님 "+courseDay + "요일 "+ startTime + " 시작일 "+startDate +" 으로 레슨을 예약하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postWaitListTask postWaitListTask = new postWaitListTask(context,courseTeacher,courseBranch,userID,startTime,userDuration,courseDay,startDate);
                                    postWaitListTask.execute("http://show981111.cafe24.com/putWaitList.php");

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startTime = "";
                                    Toast.makeText(parent.getContext(),"취소하였습니다.",Toast.LENGTH_LONG).show();
                                }
                            })
                            .create();
                    dialog.show();
                }

            }
        });


        return button;
    }

    class postWaitListTask extends AsyncTask<String, Void, String>
    {
        private Context context;

        private String pt_courseTeacher;
        private String pt_courseBranch;
        private String pt_userID;
        private String pt_startTime;
        private String pt_userDuration;
        private String pt_courseDay;
        private String pt_startDate;

        public postWaitListTask(Context context, String pt_courseTeacher, String pt_courseBranch, String pt_userID, String pt_startTime, String pt_userDuration, String pt_courseDay, String pt_startDate) {
            this.context = context;
            this.pt_courseTeacher = pt_courseTeacher;
            this.pt_courseBranch = pt_courseBranch;
            this.pt_userID = pt_userID;
            this.pt_startTime = pt_startTime;
            this.pt_userDuration = pt_userDuration;
            this.pt_courseDay = pt_courseDay;
            this.pt_startDate = pt_startDate;
        }

        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0];

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("courseBranch", pt_courseBranch)
                    .add("courseDay", pt_courseDay)
                    .add("courseTeacher", pt_courseTeacher)
                    .add("startDate",pt_startDate)
                    .add("userDuration", pt_userDuration)
                    .add("userID",pt_userID)
                    .add("startTime",pt_startTime)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Log.d("postWaitList", "going");
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("postWaitList", e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("postWaitList",s);
            if(s.equals("success"))
            {
                Toast toast = Toast.makeText(context,"성공적으로 요청되었습니다.",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if(s.equals("fail")) {
                Toast toast = Toast.makeText(context, "시작날짜와 요일을 다시한번 확인해주세요.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if(s.equals("internet_fail"))
            {
                Toast toast = Toast.makeText(context, "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if(s.equals("already"))
            {
                Toast toast = Toast.makeText(context, "이미 신청되어있습니다.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if(s.equals("alreadyBooked"))
            {
                Toast toast = Toast.makeText(context, "이미 예약되었습니다.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }



}
