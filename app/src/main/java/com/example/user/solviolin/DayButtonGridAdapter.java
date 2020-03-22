package com.example.user.solviolin;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.mMySQL.Downloader3;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.user.solviolin.MainActivity.userBranch;
import static com.example.user.solviolin.MainActivity.userDuration;
import static com.example.user.solviolin.MainActivity.userID;
import static com.example.user.solviolin.MainActivity.userName;
import static com.example.user.solviolin.mMySQL.DataParser1.BookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedList;
import static com.example.user.solviolin.mMySQL.DataParser3.personalDayBookedListcur;

public class DayButtonGridAdapter extends BaseAdapter{
    Context context = null;
    ArrayList<String> buttonNames = null;
    private Fragment parent;
    String selectedTime;
    public static int dayCredit;
    int delayDoneCredit = 0;


    String Date;





    public DayButtonGridAdapter(Context context, ArrayList<String> buttonNames, Fragment parent) {
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
//
//        if(null != convertView){
//            button = (Button)convertView;
//        }
//        else{
//            button = new Button(context);
//            button.setText(buttonNames.get(position));
//        }
//
//
//
//
//        final Button finalButton = button;
//
//        dayCredit =2;
//        if(!userDuration.equals("null") && !userDuration.isEmpty()) {
//            if (userDuration.equals("60")) {
//                dayCredit = 4;
//            }
//        }
//        int count = 0;
//        for(int i = 0 ; i < personalDayBookedList.size(); i++)
//        {
//            if(!personalDayBookedList.get(i).getNewlyBookedDate().equals("다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)") && personalDayBookedList.get(i).getDataStatus().equals("cur"))
//            {
//                count++;//이미 변경이 끝난 예약의 갯수
//            }
//        }
//
//
//        dayCredit = dayCredit - count;
//        final String dummyBookedDate = newlyBookedDate;
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                newlyBookedDate = dummyBookedDate;
//                selectedTime = "";
//                selectedTime = finalButton.getText().toString();
//                newlyBookedDate = newlyBookedDate + " " + selectedTime;
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
//                AlertDialog dialog = builder.setMessage("레슨을 변경하시겠습니까?")
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (dayCredit > 0 && !delayStatus) { //월 2회 바꾸는걸로 고쳐야됨
//                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//
//                                            try {
//                                                JSONObject jsonResponse = new JSONObject(response);
//                                                boolean success = jsonResponse.getBoolean("success");
//                                                if (success) {
//                                                    Toast.makeText(parent.getContext(), "변경이 완료되었습니다", Toast.LENGTH_LONG).show();
//                                                    notifyDataSetChanged();
//                                                    finalButton.setEnabled(false);
//                                                    Downloader3 download = new Downloader3(parent.getContext(), "http://show981111.cafe24.com/DayBookedList.php");
//                                                    download.execute();
//                                                    ((Activity)context).finish();
//
//                                                } else {
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
//                                                    AlertDialog dialog = builder.setMessage("레슨을 변경할 수 없습니다.")
//                                                            .setNegativeButton("확인", null)
//                                                            .create();
//                                                    dialog.show();
//
//                                                }
//
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//                                    };
//                                    String dataStatus = "cur";
//                                    ChangeRequest changeRequest = new ChangeRequest(canceledCourseDate,newlyBookedDate, userID, userName, BookedList.get(0).getBookedCourseTeacher(), userBranch, dataStatus, responseListener);
//                                    RequestQueue queue = Volley.newRequestQueue(parent.getContext());
//                                    queue.add(changeRequest);
//
//
//                                }else if(dayCredit <= 0 && !delayStatus){
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
//                                    AlertDialog dialog1 = builder.setMessage("레슨을 변경할 수 없습니다(변경가능 횟수가 초과하였습니다.)")
//                                            .setNegativeButton("확인",null)
//                                            .create();
//                                    dialog1.show();
//                                }
//                                else if(delayStatus){
//                                    for(int i = 0 ; i < personalDayBookedListcur.size();i++)
//                                    {
//
//                                        if(personalDayBookedListcur.get(i).getNewlyBookedDate().equals("다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)"))
//                                        {
//                                            Date = (personalDayBookedListcur.get(i).getCanceledCourseDate());
//                                            break;
//                                        }
//                                    }
//                                    final String[] dataStatus = {"cur"};
//                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
//
//                                        @Override
//                                        public void onResponse(String response) {
//
//                                            try {
//                                                JSONObject jsonResponse = new JSONObject(response);
//                                                boolean success = jsonResponse.getBoolean("success");
//                                                if (success) {
//                                                    Toast.makeText(parent.getContext(), "변경이 완료되었습니다", Toast.LENGTH_LONG).show();
//                                                    notifyDataSetChanged();
//                                                    finalButton.setEnabled(false);
//                                                    ((Activity)context).finish();
//                                                    delayStatus = false;
//                                                    final int curMonth;
//                                                    GregorianCalendar calendar = new GregorianCalendar();
//                                                    curMonth = calendar.get(Calendar.MONTH) + 1;
//                                                    if(Integer.parseInt(Date.substring(5, Date.indexOf("-", 5) )) < curMonth) {
//                                                        delayDoneCredit = delayDoneCredit + 1;
//                                                        dataStatus[0] = "old";
//                                                    }
//                                                    //new Downloader3(parent.getContext(), "http://show981111.cafe24.com/DayBookedList.php").execute();
//
//                                                } else {
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
//                                                    AlertDialog dialog = builder.setMessage("레슨을 변경할 수 없습니다.")
//                                                            .setNegativeButton("확인", null)
//                                                            .create();
//                                                    dialog.show();
//
//                                                }
//
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//                                    };
//
//                                    ChangeRequest changeRequest = new ChangeRequest(Date,newlyBookedDate, userID, userName, BookedList.get(0).getBookedCourseTeacher(), userBranch, dataStatus[0],responseListener);
//                                    RequestQueue queue = Volley.newRequestQueue(parent.getContext());
//                                    queue.add(changeRequest);
//                                    Response.Listener<String> responseListener1 = new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            try {
//                                                JSONObject jsonResponse = new JSONObject(response);
//                                                boolean success = jsonResponse.getBoolean("success");
//                                                if (success) {
//                                                    new Downloader3(parent.getContext(), "http://show981111.cafe24.com/DayBookedList.php").execute();
//
//
//
//                                                } else {
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
//                                                    AlertDialog dialog = builder.setMessage("변경이 불가능합니다.")
//                                                            .setNegativeButton("확인", null)
//                                                            .create();
//                                                    dialog.show();
//
//                                                }
//
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    };
//                                    DayCancelRequest daycancelRequest = new DayCancelRequest("다음달 중으로 할 예정(이월된 보강은 다음달까지만 가능합니다)", userID, Date,responseListener1);
//                                    RequestQueue queue1 = Volley.newRequestQueue(parent.getContext());
//                                    queue1.add(daycancelRequest);
//
//
//
//                                }
//
//                            }
//
//
//                        })
//                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(parent.getContext(),"취소하였습니다.",Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .create();
//                dialog.show();
//
//            }
//        });


        return button;
    }


}
