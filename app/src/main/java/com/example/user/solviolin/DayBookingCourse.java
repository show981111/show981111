package com.example.user.solviolin;

public class DayBookingCourse {

    String CanceledCourseDate;
    String newlyBookedDate;
    String bookedUserID;
    String bookedUserName;
    String bookedCourseTeacher;
    String dayBookedCourseBranch;
    String endDate;
    String dataStatus;

    public DayBookingCourse(String canceledCourseDate, String newlyBookedDate, String bookedUserID, String bookedUserName, String bookedCourseTeacher, String dayBookedCourseBranch, String endDate, String dataStatus) {
        CanceledCourseDate = canceledCourseDate;
        this.newlyBookedDate = newlyBookedDate;
        this.bookedUserID = bookedUserID;
        this.bookedUserName = bookedUserName;
        this.bookedCourseTeacher = bookedCourseTeacher;
        this.dayBookedCourseBranch = dayBookedCourseBranch;
        this.endDate = endDate;
        this.dataStatus = dataStatus;
    }

    public String getCanceledCourseDate() {
        return CanceledCourseDate;
    }

    public void setCanceledCourseDate(String canceledCourseDate) {
        CanceledCourseDate = canceledCourseDate;
    }

    public String getNewlyBookedDate() {
        return newlyBookedDate;
    }

    public void setNewlyBookedDate(String newlyBookedDate) {
        this.newlyBookedDate = newlyBookedDate;
    }

    public String getBookedUserID() {
        return bookedUserID;
    }

    public void setBookedUserID(String bookedUserID) {
        this.bookedUserID = bookedUserID;
    }

    public String getBookedUserName() {
        return bookedUserName;
    }

    public void setBookedUserName(String bookedUserName) {
        this.bookedUserName = bookedUserName;
    }

    public String getBookedCourseTeacher() {
        return bookedCourseTeacher;
    }

    public void setBookedCourseTeacher(String bookedCourseTeacher) {
        this.bookedCourseTeacher = bookedCourseTeacher;
    }

    public String getDayBookedCourseBranch() {
        return dayBookedCourseBranch;
    }

    public String getendDate() {
        return endDate;
    }

    public String getDataStatus() {
        return dataStatus;
    }



    public void setDayBookedCourseBranch(String dayBookedCourseBranch) {
        this.dayBookedCourseBranch = dayBookedCourseBranch;
    }
}
