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

import android.content.Intent;
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
import static com.example.user.solviolin.MainActivity.userCredit;
import static com.example.user.solviolin.MainActivity.userName;

public class ButtonGridAdapter extends BaseAdapter {
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
    private String canceledDate;

    ButtonGridAdapter myself;
    String option;


    public ButtonGridAdapter(Context context, ArrayList<String> buttonNames, Fragment parent, String courseTeacher, String startDate, String courseDay, String userID, String userBranch, String userDuration) {
        this.context = context;
        this.buttonNames = buttonNames;
        this.parent = parent;
        this.courseTeacher = courseTeacher;
        this.startDate = startDate;
        this.courseDay = courseDay;
        this.userID = userID;
        this.courseBranch = userBranch;
        this.userDuration = userDuration;
        option = "month";
    }
    public ButtonGridAdapter(Context context, ArrayList<String> buttonNames, Fragment parent, String userID, String userDuration ,String courseTeacher, String courseBranch, String startDate, String canceledDate, String option) {
        this.context = context;
        this.buttonNames = buttonNames;
        this.parent = parent;
        this.courseTeacher = courseTeacher;
        this.startDate = startDate;
        this.userID = userID;
        this.courseBranch = courseBranch;
        this.userDuration = userDuration;
        this.canceledDate = canceledDate;
        this.option = option;
        Log.d("putNewlyBookedDate",canceledDate);
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

    public void refresh(ArrayList<String> buttonNames)
    {

        this.buttonNames = buttonNames;

        notifyDataSetChanged();
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
        myself = this;
        final Button finalButton = button;


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startTime = finalButton.getText().toString();

                final String url;
                if(option.equals("month")) {
                    if (userName.equals("admin")) {
                        url = "http://show981111.cafe24.com/putRegularFromAdmin.php";
                    } else {
                        url = "http://show981111.cafe24.com/putWaitList.php";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    AlertDialog dialog = builder.setMessage(courseTeacher + "선생님 " + courseDay + "요일 " + startTime + " 시작일 " + startDate + " 으로 레슨을 예약하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    postWaitListTask postWaitListTask = new postWaitListTask(context, courseTeacher, courseBranch, userID, startTime, userDuration, courseDay, startDate, buttonNames, myself);
                                    postWaitListTask.execute(url);

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startTime = "";
                                    Toast.makeText(parent.getContext(), "취소하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            })
                            .create();
                    dialog.show();
                }else if(option.equals("day")){
                    startDate = startDate + " "+startTime;
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    AlertDialog dialog = builder.setMessage(startDate + " 에 레슨을 예약하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    putNewlyBookedDateTask putNewlyBookedDateTask = new putNewlyBookedDateTask(courseTeacher,courseBranch,userID,startDate,canceledDate,userDuration);
                                    putNewlyBookedDateTask.execute("http://show981111.cafe24.com/putNewlyDate.php");

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startTime = "";
                                    Toast.makeText(parent.getContext(), "취소하였습니다.", Toast.LENGTH_LONG).show();
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
        ButtonGridAdapter buttonGridAdapter;

        private ArrayList<String> newtimeList;
        public postWaitListTask(Context context, String pt_courseTeacher, String pt_courseBranch, String pt_userID, String pt_startTime, String pt_userDuration, String pt_courseDay, String pt_startDate,ArrayList<String> newtimeList, ButtonGridAdapter buttonGridAdapter ) {
            this.context = context;
            this.pt_courseTeacher = pt_courseTeacher;
            this.pt_courseBranch = pt_courseBranch;
            this.pt_userID = pt_userID;
            this.pt_startTime = pt_startTime;
            this.pt_userDuration = pt_userDuration;
            this.pt_courseDay = pt_courseDay;
            this.pt_startDate = pt_startDate;

            this.newtimeList = newtimeList;
            this.buttonGridAdapter = buttonGridAdapter;
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
            String message = "실패하였습니다.";
            if(s.equals("success"))
            {
//                for(String time : newtimeList)
//                {
//                    Log.d("selectedbef", time);
//                }
//                newtimeList.remove(pt_startTime);
//                for(String time : newtimeList)
//                {
//                    Log.d("selectedaf", time);
//                }
//                Log.d("selected", pt_startTime);
//                buttonGridAdapter.refresh(newtimeList);
                message = "성공적으로 요청되었습니다.";

            }else if(s.equals("fail")) {
                message = "시작날짜와 요일을 다시한번 확인해주세요.";

            }else if(s.equals("internet_fail"))
            {
                message = "인터넷 연결상태를 확인해주세요.";

            }else if(s.equals("already"))
            {
                message = "이미 신청되어있습니다.";
            }else if(s.equals("alreadyBooked"))
            {
                message = "이미 예약되었습니다.";

            }else if(s.equals("notEmpty"))
            {
                message = "비어있는 자리가 아닙니다.";

            }
            Toast toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            if(s.equals("success"))
            {
                Intent goToMain = new Intent(context, MainActivity.class);
                goToMain.putExtra("userName", userName);
                goToMain.putExtra("userID", userID);
                goToMain.putExtra("userBranch", userBranch);
                goToMain.putExtra("userCredit", userCredit);
                goToMain.putExtra("userDuration", userDuration);
                context.startActivity(goToMain);
            }
        }
    }

    class putNewlyBookedDateTask extends AsyncTask<String, Void, String>{
        private String courseTeacher;
        private String courseBranch;
        private String userID;
        private String startDate;
        private String canceledDate;
        private String userDuration;

        public putNewlyBookedDateTask(String courseTeacher, String courseBranch, String userID, String startDate, String canceledDate,String userDuration) {
            this.courseTeacher = courseTeacher;
            this.courseBranch = courseBranch;
            this.userID = userID;
            this.startDate = startDate;
            this.canceledDate = canceledDate;
            this.userDuration = userDuration;
            Log.d("putNewlyBookedDateTask", canceledDate);
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("courseTeacher", courseTeacher)
                    .add("courseBranch", courseBranch)
                    .add("userID", userID)
                    .add("startDate",startDate)
                    .add("canceledDate", canceledDate)
                    .add("userDuration",userDuration)
                    .add("userName",userName)
                    .build();

//            Log.d("putNewlyBookedDateTaskT",courseTeacher);
//            Log.d("putNewlyBookedDateTaskB",courseBranch);
//            Log.d("putNewlyBookedDateTaskI",userID);
//            Log.d("putNewlyBookedDateTaskD",startDate);
//            Log.d("putNewlyBookedDateTaskC",canceledDate);
//            Log.d("putNewlyBookedDateTaskU",userDuration);
//            Log.d("putNewlyBookedDateTaskN",userName);

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Log.d("putNewlyBookedDateTask", "going");
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("putNewlyBookedDateTask", e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("putNewlyBookedDateTask",s);
            String message = "실패하였습니다.";
            if(s.equals("success"))
            {
                message = "성공적으로 변경하였습니다.";
            }else if(s.equals("past") || s.equals("future")) {
                message = "날짜를 다시한번 확인해주세요";

            }else if(s.equals("isBooked"))
            {
                message = "빈 시간대가 아닙니다.";
            }else if(s.equals("fail"))
            {
                message = "실패하였습니다.";

            }else if(s.equals("already")) {
                message = "이미 변경이 되어있습니다.";
            }
            Toast toast = Toast.makeText(context,message,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            if(s.equals("success"))
            {
                Intent goToMain = new Intent(context, MainActivity.class);
                goToMain.putExtra("userName", userName);
                goToMain.putExtra("userID", userID);
                goToMain.putExtra("userBranch", userBranch);
                goToMain.putExtra("userCredit", userCredit);
                goToMain.putExtra("userDuration", userDuration);
                context.startActivity(goToMain);

            }
        }
    }




}
