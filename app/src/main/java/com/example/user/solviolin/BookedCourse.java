package com.example.user.solviolin;

public class BookedCourse {

    String BookedCourseTeacher;
    String BookedCourseDay;
    String BookedCourseTime;
    int BookedCourseID;
    String BookingUserID;
    String BookedCourseBranch;
    String startDate;


    public String getBookedCourseBranch() {
        return BookedCourseBranch;
    }

    public void setBookedCourseBranch(String bookedCourseBranch) {
        BookedCourseBranch = bookedCourseBranch;
    }

    public String getBookingUserID() {
        return BookingUserID;
    }

    public void setBookingUserID(String bookingUserID) {
        BookingUserID = bookingUserID;
    }

    public int getBookedCourseID() {
        return BookedCourseID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setBookedCourseID(int bookedCourseID) {
        BookedCourseID = bookedCourseID;
    }




    public BookedCourse(String bookingUserID, String bookedCourseTeacher, String bookedCourseDay, String bookedCourseTime, int bookedCourseID, String bookedCourseBranch, String startDate) {
        this.BookingUserID = bookingUserID;
        this.BookedCourseTeacher = bookedCourseTeacher;
        this.BookedCourseDay = bookedCourseDay;
        this.BookedCourseTime = bookedCourseTime;
        this.BookedCourseID = bookedCourseID;
        this.BookedCourseBranch = bookedCourseBranch;
        this.startDate = startDate;

    }

    public String getBookedCourseTeacher() {
        return BookedCourseTeacher;
    }

    public void setBookedCourseTeacher(String bookedCourseTeacher) {
        BookedCourseTeacher = bookedCourseTeacher;
    }

    public String getBookedCourseDay() {
        return BookedCourseDay;
    }

    public void setBookedCourseDay(String bookedCourseDay) {
        BookedCourseDay = bookedCourseDay;
    }

    public String getBookedCourseTime() {
        return BookedCourseTime;
    }

    public void setBookedCourseTime(String bookedCourseTime) {
        BookedCourseTime = bookedCourseTime;
    }


}
