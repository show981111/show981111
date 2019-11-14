package com.example.user.solviolin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DayCancelRequest extends StringRequest{

    final static private String URL = "http://show981111.cafe24.com/DayCancel.php";
    private Map<String,String> parameters;

    public DayCancelRequest(String newlyBookedDate, String userID, String canceledCourseDate , Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("newlyBookedDate", newlyBookedDate);
        parameters.put("userID", userID);
        parameters.put("canceledCourseDate", canceledCourseDate);
    }

    public Map<String, String> getParams(){
        return parameters;
    }
}
