package com.example.user.solviolin.adapter;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.Data.User;
import com.example.user.solviolin.R;
import com.example.user.solviolin.getData.DeleteRequest;


import org.json.JSONObject;

import java.util.List;


public class UserListAdapter extends BaseAdapter{

    private Context context;
    private List<User> userList;
    private AppCompatActivity parentActivity;
    private List<User> saveList;
    public static String AdminUserID;






    public UserListAdapter(Context context, List<User> userList, AppCompatActivity parentActivity, List<User> saveList){
        this.context = context;
        this.userList = userList;
        this.parentActivity = parentActivity;
        this.saveList = saveList;


    }




    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.user, null);
        final TextView userID = (TextView) v.findViewById(R.id.userID);
        final TextView userPassword = (TextView) v.findViewById(R.id.userPassword);
        final TextView userName = (TextView) v.findViewById(R.id.userName);
        final TextView userPhone = (TextView) v.findViewById(R.id.userPhone);

        userID.setText(userList.get(position).getUserID());
        userPassword.setText(userList.get(position).getUserPassword());
        userName.setText(userList.get(position).getUserName());
        userPhone.setText(userList.get(position).getUserPhone());



        v.setTag(userList.get(position).getUserID());
        Button checkResultButton = (Button) v.findViewById(R.id.CheckResultButton);

        checkResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    public void onResponse(String response){
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                userList.remove(position);
                                for(int i = 0; i < saveList.size(); i++){
                                    if(saveList.get(i).getUserID().equals(userID.getText().toString())){
                                        saveList.remove(i);
                                        break;
                                    }
                                }
                                notifyDataSetChanged();
                                // ((ManagementActivity)context).runOnUiThread(new Runnable() {
                                //                                    @Override
                                //                                    public void run() {
                                //                                        notifyDataSetChanged();
                                //                                    }
                                //                                });
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }


                    }

                };
                DeleteRequest deleteRequest = new DeleteRequest(userID.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                queue.add(deleteRequest);


            }
        });
        notifyDataSetChanged();
        return v;



    }


}
