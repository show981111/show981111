package com.example.user.solviolin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookedListRequest extends StringRequest{

    final static private String URL = "http://show981111.cafe24.com/GetRegularList.php";
    private Map<String, String> parameters;

    public BookedListRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);


    }

    public Map<String, String> getParams() {
        return parameters;
    }
}


