package com.example.user.solviolin;

public class Open {
    String openStartDate;
    String openEndDate;
    String openBranch;
    String openTeacher;

    public Open(String openStartDate, String openEndDate, String openBranch, String openTeacher) {
        this.openStartDate = openStartDate;
        this.openEndDate = openEndDate;
        this.openBranch = openBranch;
        this.openTeacher = openTeacher;
    }

    public String getOpenStartDate() {
        return openStartDate;
    }

    public void setOpenStartDate(String openStartDate) {
        this.openStartDate = openStartDate;
    }

    public String getOpenEndDate() {
        return openEndDate;
    }

    public void setOpenEndDate(String openEndDate) {
        this.openEndDate = openEndDate;
    }

    public String getOpenBranch() {
        return openBranch;
    }

    public void setOpenBranch(String openBranch) {
        this.openBranch = openBranch;
    }

    public String getOpenTeacher() {
        return openTeacher;
    }

    public void setOpenTeacher(String openTeacher) {
        this.openTeacher = openTeacher;
    }
}
