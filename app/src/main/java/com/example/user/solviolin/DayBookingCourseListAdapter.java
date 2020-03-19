package com.example.user.solviolin;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedListcur;


public class DayBookingCourseListAdapter extends BaseAdapter{

    private Context context;
    private List<DayBookingCourse> DayBookedList;
    private AppCompatActivity parentActivity;







    public DayBookingCourseListAdapter(Context context, List<DayBookingCourse> DayBookedCourseList, AppCompatActivity parentActivity){
        this.context = context;
        this.DayBookedList = DayBookedCourseList;
        this.parentActivity = parentActivity;



    }




    @Override
    public int getCount() {
        return DayBookedList.size();
    }

    @Override
    public Object getItem(int position) {
        return DayBookedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       // @SuppressLint("ViewHolder") View v = View.inflate(context, result, null);
        View v = View.inflate(context,R.layout.daybooking, null);
        final TextView DayBookedCourseTeacher = (TextView) v.findViewById(R.id.DayBookedCourseTeacher);
        final TextView newlyBookedDate = (TextView) v.findViewById(R.id.newlyBookedDate);
        final TextView canceledBookedDate = (TextView) v.findViewById(R.id.canceledDate);


        if(DayBookedList.size() > 0) {

            DayBookedCourseTeacher.setText(DayBookedList.get(position).getBookedCourseTeacher().toString());
            canceledBookedDate.setText(DayBookedList.get(position).getCanceledCourseDate() + " -->>");
            newlyBookedDate.setText(DayBookedList.get(position).getNewlyBookedDate().toString());
        }
        final Button daycancelButton = (Button) v.findViewById(R.id.dayCancelButton);
        if(!userName.equals("admin"))
        {
            daycancelButton.setVisibility(View.GONE);
        }





        daycancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int curYear,curMonth,curDay;
                GregorianCalendar calendar = new GregorianCalendar();
                curYear = calendar.get(Calendar.YEAR);
                curMonth = calendar.get(Calendar.MONTH);
                curDay= calendar.get(Calendar.DAY_OF_MONTH);
                int startpoint = DayBookedList.get(position).getNewlyBookedDate().lastIndexOf(" ");
                int length = DayBookedList.get(position).getNewlyBookedDate().length();
                Boolean timeout = false;
                int curtime;
                String curDate = String.valueOf(curYear) + "-" + String.valueOf(curMonth + 1) + "-" + String.valueOf(curDay);
                if(curDate.equals(DayBookedList.get(position).getNewlyBookedDate().substring(0,startpoint)))
                {
                    Calendar calendar2 = Calendar.getInstance();

                    curtime =  calendar2.get(Calendar.HOUR_OF_DAY)*100 + calendar2.get(Calendar.MINUTE);
                    int bookingTime =  Integer.parseInt(DayBookedList.get(position).getNewlyBookedDate().substring(startpoint + 1,startpoint+3))*100 + Integer.parseInt(DayBookedList.get(position).getNewlyBookedDate().substring(startpoint+4, length ));
                    if(bookingTime - curtime <400)
                    {
                        timeout =true;

                    }

                }
                if(!timeout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    AlertDialog dialog = builder.setMessage("정말 레슨 변경을 취소하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(parentActivity, "변경이 취소되었습니다.", Toast.LENGTH_LONG).show();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                                                    personalDayBookedListcur.remove(position);
                                                    notifyDataSetChanged();

                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                                                    AlertDialog dialog = builder.setMessage("변경을 취소할 수 없습니다.")
                                                            .setNegativeButton("확인", null)
                                                            .create();
                                                    dialog.show();

                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    DayCancelRequest daycancelRequest = new DayCancelRequest(DayBookedList.get(position).getNewlyBookedDate(), DayBookedList.get(position).getBookedUserID(), DayBookedList.get(position).getCanceledCourseDate(), responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(parentActivity);
                                    queue.add(daycancelRequest);

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(parentActivity, "취소하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            })
                            .create();
                    dialog.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    AlertDialog dialog = builder.setMessage("취소는 레슨 시간 4시간 전까지만 가능합니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }





            }
        });

        notifyDataSetChanged();
        return v;



    }


}
