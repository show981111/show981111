package com.example.user.solviolin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class initializeRequest extends StringRequest{

    final static private String URL = "http://show981111.cafe24.com/initialize.php";


    public initializeRequest(Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
    }


}
