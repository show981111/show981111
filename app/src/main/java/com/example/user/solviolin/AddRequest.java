package com.example.user.solviolin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    final static private String URL = "http://show981111.cafe24.com/CourseAdd.php";
    private Map<String, String> parameters;

    public AddRequest(String userID, String courseID, String userBranch, String userName, String courseTeacher, String courseDay, String courseTime, String dow, String startDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("courseID", courseID);
        parameters.put("userBranch", userBranch);
        parameters.put("userName", userName);
        parameters.put("courseTeacher", courseTeacher);
        parameters.put("courseDay", courseDay);
        parameters.put("courseTime", courseTime);
        parameters.put("dow", dow);
        parameters.put("startDate", startDate);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
