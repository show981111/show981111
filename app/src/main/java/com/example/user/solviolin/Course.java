package com.example.user.solviolin;

public class Course {

    int courseID;
    String courseTeacher;
    String courseDay;
    String courseTime;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(String courseDay) {
        this.courseDay = courseDay;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public Course(int courseID, String courseTeacher, String courseDay, String courseTime) {
        this.courseID = courseID;
        this.courseTeacher = courseTeacher;
        this.courseDay = courseDay;
        this.courseTime = courseTime;
    }
}
