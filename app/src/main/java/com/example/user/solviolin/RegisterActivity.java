package com.example.user.solviolin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;

    private boolean validate = false;
    private AlertDialog dialog;
    private String userType;
    private String userID;
    private String userPassword;
    private String userName;
    private String userPhone;
    private String userDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        spinner = (Spinner) findViewById(R.id.branchSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_gallery_item);
        spinner.setAdapter(adapter);

        final EditText idTextR = (EditText) findViewById(R.id.idTextR);
        final EditText passwordTextR = (EditText) findViewById(R.id.passwordTextR);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText codeText = (EditText) findViewById(R.id.codeText);
        RadioGroup durationGroup = (RadioGroup) findViewById(R.id.durationGroup);
        RadioGroup typeGroup = (RadioGroup) findViewById(R.id.typeGroup);

        int typeGroupID = typeGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(typeGroupID);
        userType = rb.getText().toString();

        int durationID = durationGroup.getCheckedRadioButtonId();
        RadioButton rb1 = (RadioButton) findViewById(durationID);
        userDuration = rb1.getText().toString();


        final Button validateButton = (Button) findViewById(R.id.validateButton);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idTextR.getText().toString();
                if(validate)
                {
                    return;
                }
                else if(userID.equals("")){
                     AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                     dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                             .setPositiveButton("확인",null)
                             .create();
                     dialog.show();
                     return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                idTextR.setEnabled(false);
                                validate = true;
                                idTextR.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();

                            }

                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });





        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String userID = idTextR.getText().toString();
                String userPassword = passwordTextR.getText().toString();
                String userName = nameText.getText().toString();
                String userPhone = phoneText.getText().toString();
                String userBranch = spinner.getSelectedItem().toString();
                String code = codeText.getText().toString();



                if(!validate)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                if(userID.equals("") || userPassword.equals("") || userName.equals("") || userPhone.equals("") || userType.equals("") || code.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;

                }else if(!code.equals("cjsol0731"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("가입코드를 정확히 입력해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try{


                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    AlertDialog dialog = builder.setMessage("회원 가입에 성공하였습니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create();
                                    dialog.show();


                                }
                                else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 가입에 실패하였습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();

                                }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userPhone, userType, userBranch, userDuration, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }



        });

    }

    protected void onStop(){
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}
