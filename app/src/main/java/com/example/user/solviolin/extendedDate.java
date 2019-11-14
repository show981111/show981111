package com.example.user.solviolin;

public class extendedDate {

    String extendedDate;
    String endDate;
    String extendedUserID;
    String extendedcourseTeacher;
    String extendeduserBranch;

    public extendedDate(String extendedDate, String endDate, String extendedUserID, String extendedcourseTeacher, String extendeduserBranch) {
        this.extendedDate = extendedDate;
        this.endDate = endDate;
        this.extendedUserID = extendedUserID;
        this.extendedcourseTeacher = extendedcourseTeacher;
        this.extendeduserBranch = extendeduserBranch;
    }

    public String getExtendedDate() {
        return extendedDate;
    }

    public void setExtendedDate(String extendedDate) {
        this.extendedDate = extendedDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExtendedUserID() {
        return extendedUserID;
    }

    public void setExtendedUserID(String extendedUserID) {
        this.extendedUserID = extendedUserID;
    }

    public String getExtendedcourseTeacher() {
        return extendedcourseTeacher;
    }

    public void setExtendedcourseTeacher(String extendedcourseTeacher) {
        this.extendedcourseTeacher = extendedcourseTeacher;
    }

    public String getExtendeduserBranch() {
        return extendeduserBranch;
    }

    public void setExtendeduserBranch(String extendeduserBranch) {
        this.extendeduserBranch = extendeduserBranch;
    }
}
