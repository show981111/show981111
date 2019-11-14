package com.example.user.solviolin;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userCredit;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.MonthFragment.Timeindex;
import static com.example.user.solviolin.MonthFragment.dow;
import static com.example.user.solviolin.MonthFragment.selectedDay;
import static com.example.user.solviolin.MonthFragment.selectedTeacher;
import static com.example.user.solviolin.MonthFragment.startDate;
import static com.example.user.solviolin.mMySQL.DataParser.courseID;
import static com.example.user.solviolin.mMySQL.DataParser.courseTime;
import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;

public class ButtonGridAdapter extends BaseAdapter{
    Context context = null;
    ArrayList<String> buttonNames = null;
    String selectedTime;
    private Fragment parent;


    private int IDindex;

    public ButtonGridAdapter(Context context, ArrayList<String> buttonNames, Fragment parent) {
        this.context = context;
        this.buttonNames = buttonNames;
        this.parent = parent;
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

                selectedTime = finalButton.getText().toString();
                for(int i = 0 ; i < Timeindex.size(); i++)
                {
                    if(courseTime.get(Timeindex.get(i)).equals(selectedTime))
                    {
                        IDindex = Timeindex.get(i);
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                AlertDialog dialog = builder.setMessage("레슨을 예약하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int count = BookedList.size();
                                if (count < userCredit) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(parent.getContext(), "예약이 완료되었습니다", Toast.LENGTH_LONG).show();
                                                    notifyDataSetChanged();
                                                    finalButton.setEnabled(false);
                                                    ((Activity)context).finish();

                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                                    selectedTime = "";
                                                    AlertDialog dialog = builder.setMessage("레슨을 예약할 수 없습니다(이미 예약된 시간대입니다).")
                                                            .setNegativeButton("확인", null)
                                                            .create();
                                                    dialog.show();

                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    };
                                    startDate = startDate + " "+ selectedTime;
                                    AddRequest addRequest = new AddRequest(userID, courseID.get(IDindex) + " ", userBranch, userName, selectedTeacher, selectedDay, selectedTime, dow+ "", startDate, responseListener);// 여기에 해당 코스 아이디 삽입 필요
                                    RequestQueue queue = Volley.newRequestQueue(parent.getContext());
                                    queue.add(addRequest);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                    selectedTime = "";
                                    AlertDialog dialog1 = builder.setMessage("레슨을 예약할 수 없습니다(수강가능 횟수가 초과하였습니다.)")
                                            .setNegativeButton("확인",null)
                                            .create();
                                    dialog1.show();
                                }
                            }


                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedTime = "";
                                Toast.makeText(parent.getContext(),"취소하였습니다.",Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                dialog.show();



               /* Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                AlertDialog dialog = builder.setMessage("레슨을 예약하시겠습니까?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(parent.getContext(),"예약이 완료되었습니다",Toast.LENGTH_LONG).show();
                                                
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(parent.getContext(),"취소하였습니다.",Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .create();

                                dialog.show();

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                AlertDialog dialog = builder.setMessage("레슨을 예약할 수 없습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                            }

                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };*/
                /*AddRequest addRequest = new AddRequest(userID, courseID.get(IDindex) + " ", userBranch, userName, selectedTeacher,responseListener);// 여기에 해당 코스 아이디 삽입 필요
                RequestQueue queue = Volley.newRequestQueue(parent.getContext());
                queue.add(addRequest);*/

            }
        });


        return button;
    }


}
