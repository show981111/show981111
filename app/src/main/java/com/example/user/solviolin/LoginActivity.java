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
                Log.d("loginTask", "got");
                return client;

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("loginTask", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(userData[] client) {
            super.onPostExecute(client);

            for(userData item : client)
            {
                userDataArrayList.add(item);
            }
            if(userDataArrayList.size() > 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userName", userDataArrayList.get(0).getUserName());
                intent.putExtra("userID", userDataArrayList.get(0).getUserID());
                intent.putExtra("userBranch", userDataArrayList.get(0).getUserBranch());
                intent.putExtra("userDuration", userDataArrayList.get(0).getUserDuration());
                Log.d("login",userDataArrayList.get(0).getUserName());
                Log.d("login",userDataArrayList.get(0).getUserID());
                Log.d("login",userDataArrayList.get(0).getUserBranch());
                Log.d("login",userDataArrayList.get(0).getUserDuration());
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
