package com.example.user.solviolin;

public class BookedList {
    private String bookedTeacher;
    private String bookedStartDate;
    private String bookedEndDate;
    private String bookedBranch;
    private String status;


    public BookedList(String bookedTeacher, String bookedStartDate, String bookedEndDate, String bookedBranch,String status) {
        this.bookedTeacher = bookedTeacher;
        this.bookedStartDate = bookedStartDate;
        this.bookedEndDate = bookedEndDate;
        this.bookedBranch = bookedBranch;
        this.status = status;
    }

    public String getBookedBranch() {
        return bookedBranch;
    }

    public void setBookedBranch(String bookedBranch) {
        this.bookedBranch = bookedBranch;
    }

    public String getBookedTeacher() {
        return bookedTeacher;
    }

    public void setBookedTeacher(String bookedTeacher) {
        this.bookedTeacher = bookedTeacher;
    }

    public String getBookedStartDate() {
        return bookedStartDate;
    }

    public void setBookedStartDate(String bookedStartDate) {
        this.bookedStartDate = bookedStartDate;
    }

    public String getBookedEndDate() {
        return bookedEndDate;
    }

    public void setBookedEndDate(String bookedEndDate) {
        this.bookedEndDate = bookedEndDate;
    }
}
