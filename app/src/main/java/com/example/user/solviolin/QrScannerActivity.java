package com.example.user.solviolin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.solviolin.MainActivity.getToken;
import static com.example.user.solviolin.MainActivity.userID;

public class QrScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);


        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "취소되었습니다!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("QRbefore", userID);
                Log.d("QRbefore", result.getContents());
                Log.d("QRbefore", getToken);
                QrcheckIn qrcheckIn = new QrcheckIn(userID,result.getContents() ,getToken);
                qrcheckIn.execute("http://show981111.cafe24.com/qrCheckIn.php");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    class QrcheckIn extends AsyncTask<String , Void, String >{

        private String userID;
        private String userBranch;
        private String token;

        public QrcheckIn(String userID, String userBranch, String token) {
            this.userID = userID;
            this.userBranch = userBranch;
            this.token = token;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];

            OkHttpClient client = new OkHttpClient();
            Log.d("QRafter", userID);
            Log.d("QRafter", userBranch);
            Log.d("QRafter", token);

            RequestBody formBody = new FormBody.Builder()
                    .add("userID", userID)
                    .add("userBranch", userBranch)
                    .add("token", token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Qrafter", s);
            if(s.equals("success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(QrScannerActivity.this);
                builder.setMessage("체크인에 성공하였습니다!")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goToMainIntent = new Intent(QrScannerActivity.this, MainActivity.class);
                                QrScannerActivity.this.startActivity(goToMainIntent);
                            }
                        })
                       .create()
                        .show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(QrScannerActivity.this);
                builder.setMessage("체크인에 실패하였습니다. 다시 시도해주세요!")
                        .setNegativeButton("다시 시도", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent goToMainIntent = new Intent(QrScannerActivity.this, MainActivity.class);
                                QrScannerActivity.this.startActivity(goToMainIntent);
                            }
                        })
                        .create()
                        .show();
            }
        }
    }
}


