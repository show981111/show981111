package com.example.user.solviolin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CancelRequest extends StringRequest{

    final static private String URL = "http://show981111.cafe24.com/Cancel.php";
    private Map<String,String> parameters;

    public CancelRequest(String courseID, String courseBranch, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("courseID", courseID);
        parameters.put("courseBranch", courseBranch);
    }

    public Map<String, String> getParams(){
        return parameters;
    }
}
