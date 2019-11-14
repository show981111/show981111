package com.example.user.solviolin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangeRequest extends StringRequest {

    final static private String URL = "http://show981111.cafe24.com/CourseChange.php";
    private Map<String, String> parameters;

    public ChangeRequest(String canceledCourseDate, String newlyBookedDate, String userID, String userName, String courseTeacher, String userBranch, String dataStatus, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("canceledCourseDate", canceledCourseDate);
        parameters.put("newlyBookedDate", newlyBookedDate);
        parameters.put("userID", userID);
        parameters.put("userName", userName);
        parameters.put("courseTeacher", courseTeacher);
        parameters.put("userBranch", userBranch);
        parameters.put("dataStatus", dataStatus);


    }

    public Map<String, String> getParams() {
        return parameters;
    }
}
