package com.example.user.solviolin;

import android.widget.Button;

public class waitlist_item {
    private String wl_userID;
    private String wl_userBranch;
    private String wl_courseTeacher;
    private String wl_startDate;
    private String wl_Time;

    public waitlist_item(String wl_userID, String wl_userBranch, String wl_courseTeacher, String wl_startDate, String wl_Time) {
        this.wl_userID = wl_userID;
        this.wl_userBranch = wl_userBranch;
        this.wl_courseTeacher = wl_courseTeacher;
        this.wl_startDate = wl_startDate;
        this.wl_Time = wl_Time;

    }

    public String getWl_userID() {
        return wl_userID;
    }

    public void setWl_userID(String wl_userID) {
        this.wl_userID = wl_userID;
    }

    public String getWl_userBranch() {
        return wl_userBranch;
    }

    public void setWl_userBranch(String wl_userBranch) {
        this.wl_userBranch = wl_userBranch;
    }

    public String getWl_courseTeacher() {
        return wl_courseTeacher;
    }

    public void setWl_courseTeacher(String wl_courseTeacher) {
        this.wl_courseTeacher = wl_courseTeacher;
    }

    public String getWl_startDate() {
        return wl_startDate;
    }

    public void setWl_startDate(String wl_startDate) {
        this.wl_startDate = wl_startDate;
    }

    public String getWl_Time() {
        return wl_Time;
    }

    public void setWl_Time(String wl_Time) {
        this.wl_Time = wl_Time;
    }
}
