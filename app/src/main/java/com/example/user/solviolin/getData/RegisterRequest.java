package com.example.user.solviolin.getData;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://show981111.cafe24.com/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userName, String userPhone, String userType, String userBranch, String userDuration, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userPhone", userPhone);
        parameters.put("userType", userType);
        parameters.put("userBranch", userBranch);
        parameters.put("userDuration", userDuration);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
