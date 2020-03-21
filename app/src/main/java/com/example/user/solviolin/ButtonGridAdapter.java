package com.example.user.solviolin;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.user.solviolin.MainActivity.userName;
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

                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                AlertDialog dialog = builder.setMessage("레슨을 예약하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int count = BookedList.size();
                                if (count > 0) {
                                    ///startDate = startDate + " "+ selectedTime;
                                    Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                            "mailto","solvn@naver.com", null));

                                    // email setting 배열로 해놔서 복수 발송 가능
                                    //String[] address = {"solvn@naver.com"};
                                    //email.putExtra(Intent.EXTRA_EMAIL, address);
                                    email.putExtra(Intent.EXTRA_SUBJECT,"["+userName+ "] 정기예약 변경 요청");
                                    int dow = 1;//SHOULD BE FIXED
                                    //email.putExtra(Intent.EXTRA_TEXT,"아이디 : "+ userID +"\n 지점: "+ userBranch + "\n 선생님: "+ selectedTeacher + "\n 요일: "+ selectedDay + "\n 시간: " + selectedTime + "\n dow: " + dow + "\n 시작 날짜: " + startDate + " 으로 변경 요청합니다." );
                                    try{
                                        context.startActivity(email);
                                        Toast.makeText(parent.getContext(), "이메일로 이동합니다.", Toast.LENGTH_LONG).show();
                                        notifyDataSetChanged();
                                        finalButton.setEnabled(false);
                                        ((Activity)context).finish();
                                    }catch (android.content.ActivityNotFoundException ex){
                                        Toast.makeText(parent.getContext(),"변경 요청을 보낼 수 없습니다.",Toast.LENGTH_LONG).show();
                                    }

                                    /*Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    Intent email = new Intent(Intent.ACTION_SEND);
                                                    email.setType("plain/text");
                                                    // email setting 배열로 해놔서 복수 발송 가능
                                                    String[] address = {"show981111@naver.com"};
                                                    email.putExtra(Intent.EXTRA_EMAIL, address);
                                                    email.putExtra(Intent.EXTRA_SUBJECT,"보내질 email 제목");
                                                    email.putExtra(Intent.EXTRA_TEXT,"보낼 email 내용을 미리 적어 놓을 수 있습니다.\n");
                                                    context.startActivity(email);

                                                    Toast.makeText(parent.getContext(), "변경 요청이 완료되었습니다", Toast.LENGTH_LONG).show();
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
                                    };*/

                                    /*AddRequest addRequest = new AddRequest(userID, courseID.get(IDindex) + " ", userBranch, userName, selectedTeacher, selectedDay, selectedTime, dow+ "", startDate, responseListener);// 여기에 해당 코스 아이디 삽입 필요
                                    RequestQueue queue = Volley.newRequestQueue(parent.getContext());
                                    queue.add(addRequest);*/
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                    selectedTime = "";
                                    AlertDialog dialog1 = builder.setMessage("기존에 수업이 있는지 확인해주세요.")
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
            }
        });


        return button;
    }


}
