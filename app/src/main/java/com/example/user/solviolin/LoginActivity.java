package com.example.user.solviolin;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.solviolin.Data.BookedList;
import com.example.user.solviolin.Data.termList;
import com.example.user.solviolin.Data.userData;
import com.example.user.solviolin.getData.LoginRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static String token;
    private static String userID;
    private static String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final TextView registerTextView = (TextView) findViewById(R.id.registerTextView);
        final Button loginButton = (Button) findViewById(R.id.loginButton);


        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = idText.getText().toString();
                userPassword = passwordText.getText().toString();
                loginTask loginTask = new loginTask();
                loginTask.execute("http://show981111.cafe24.com/loginSetToken.php");

//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try{
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//                            if(success){
//                                String userID = jsonResponse.getString("userID");
//                                String userBranch = jsonResponse.getString("userBranch");
//                                String userName = jsonResponse.getString("userName");
//                                String userDuration = jsonResponse.getString("userDuration");
//                                int userCredit = jsonResponse.getInt("userCredit");
//                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
//                                    @Override
//                                    public void onSuccess(InstanceIdResult instanceIdResult) {
//                                        token = instanceIdResult.getToken();
//                                        Log.d("token",token);
//
//                                    }
//                                });
//
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("userName", userName);
//                                intent.putExtra("userID", userID);
//                                intent.putExtra("userBranch", userBranch);
//                                intent.putExtra("userCredit", userCredit);
//                                intent.putExtra("userDuration", userDuration);
////                                intent.putExtra("token", token);
////                                Log.d("token",token);
//                                LoginActivity.this.startActivity(intent);
//                            }
//                            else{
//                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                builder.setMessage("로그인에 실패하였습니다")
//                                        .setNegativeButton("다시 시도", null)
//                                        .create()
//                                        .show();
//                            }
//
//                        }catch(Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//
//                    }
//                };
//                LoginRequest loginRequest = new LoginRequest(userID, userPassword ,responseListener);
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                queue.add(loginRequest);



            }
        });




    }

    class loginTask extends AsyncTask<String, Void, userData[]>{

        private ArrayList<userData> userDataArrayList = new ArrayList<>();
        @Override
        protected userData[] doInBackground(String... strings) {
            String url = strings[0];
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    token = instanceIdResult.getToken();
                    Log.d("token",token);

                }
            });

            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("userID", userID )
                    .add("userPassword", userPassword)
                    .add("token",token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                Log.d("loginTask", "res");
                Gson gson = new Gson();
                userData[] client = gson.fromJson(response.body().charStream(), userData[].class);
                return client;

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("loginTask", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(userData[] userDatas) {
            super.onPostExecute(userDatas);

            for(userData item : userDatas)
            {
                userDataArrayList.add(item);
            }

            if(userDataArrayList.size() > 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userName", userDataArrayList.get(0).getUserName());
                intent.putExtra("userID", userDataArrayList.get(0).getUserName());
                intent.putExtra("userBranch", userDataArrayList.get(0).getUserName());
                intent.putExtra("userDuration", userDataArrayList.get(0).getUserName());
                LoginActivity.this.startActivity(intent);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("로그인에 실패하였습니다")
                        .setNegativeButton("다시 시도", null)
                        .create()
                        .show();
            }
        }
    }
}
