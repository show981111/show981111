package com.example.user.solviolin;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest{

    final static private String URL = "http://show981111.cafe24.com/Delete.php";
    private Map<String,String> parameters;

    public DeleteRequest(String userID, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    public Map<String, String> getParams(){
        return parameters;
    }
}
