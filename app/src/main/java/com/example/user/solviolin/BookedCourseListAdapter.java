package com.example.user.solviolin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;


import static com.example.user.solviolin.MainActivity.userCredit;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.R.layout.notification_template_part_chronometer;
import static com.example.user.solviolin.R.layout.result;


public class BookedCourseListAdapter extends BaseAdapter{

    private Context context;
    private List<BookedCourse> BookedList;
    private Activity parentActivity;







    public BookedCourseListAdapter(Context context, List<BookedCourse> BookedCourseList, Activity parentActivity){
        this.context = context;
        this.BookedList = BookedCourseList;
        this.parentActivity = parentActivity;



    }




    @Override
    public int getCount() {
        return BookedList.size();
    }

    @Override
    public Object getItem(int position) {
        return BookedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       // @SuppressLint("ViewHolder") View v = View.inflate(context, result, null);
        View v = View.inflate(context,R.layout.result, null);
        final View item = v;
        final TextView BookedCourseTeacher = (TextView) v.findViewById(R.id.BookedCourseTeacher);
        final TextView BookedCourseDay = (TextView) v.findViewById(R.id.BookedCourseDay);
        final TextView BookedCourseTime = (TextView) v.findViewById(R.id.BookedCourseTime);



        BookedCourseTeacher.setText(BookedList.get(position).getBookedCourseTeacher().toString());
        BookedCourseDay.setText(BookedList.get(position).getBookedCourseDay().toString());
        BookedCourseTime.setText(BookedList.get(position).getBookedCourseTime().toString());



        final Button cancelButton = (Button) v.findViewById(R.id.cancelButton);
        if(!userName.equals("admin"))
        {
            cancelButton.setVisibility(View.GONE);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                AlertDialog dialog = builder.setMessage("정말 레슨 예약을 취소하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try
                                        {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success){
                                                cancelButton.setEnabled(false);
                                                Toast.makeText(parentActivity,"예약이 취소되었습니다.",Toast.LENGTH_LONG).show();
                                                BookedList.remove(position);
                                                notifyDataSetChanged();

                                            }
                                            else{
                                                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                                                AlertDialog dialog = builder.setMessage("예약을 취소할 수 없습니다.")
                                                        .setNegativeButton("확인",null)
                                                        .create();
                                                dialog.show();

                                            }

                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }};
                                CancelRequest cancelRequest = new CancelRequest(BookedList.get(position).getBookedCourseID() +" ",BookedList.get(position).getBookedCourseBranch().toString(), responseListener);
                                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                                queue.add(cancelRequest);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(parentActivity,"취소하였습니다.",Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                dialog.show();

                /*Response.Listener<String> responseListener = new Response.Listener<String>(){
                    public void onResponse(String response){
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                BookedList.remove(position);
                                notifyDataSetChanged();

                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }


                    }

                };
                CancelRequest cancelRequest = new CancelRequest(BookedList.get(position).getBookedCourseID() +" ",BookedList.get(position).getBookedCourseBranch().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                queue.add(cancelRequest);*/
                notifyDataSetChanged();

            }
        });
        notifyDataSetChanged();
        return v;



    }


}
