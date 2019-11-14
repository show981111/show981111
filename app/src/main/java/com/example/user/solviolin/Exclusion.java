package com.example.user.solviolin;

public class Exclusion {
    String closedStartDate;
    String closedEndDate;
    String closedBranch;
    String closedTeacher;

    public Exclusion(String closedStartDate, String closedEndDate, String closedBranch, String closedTeacher) {
        this.closedStartDate = closedStartDate;
        this.closedEndDate = closedEndDate;
        this.closedBranch = closedBranch;
        this.closedTeacher = closedTeacher;
    }

    public String getClosedStartDate() {
        return closedStartDate;
    }

    public void setClosedStartDate(String closedStartDate) {
        this.closedStartDate = closedStartDate;
    }

    public String getClosedEndDate() {
        return closedEndDate;
    }

    public void setClosedEndDate(String closedEndDate) {
        this.closedEndDate = closedEndDate;
    }

    public String getClosedBranch() {
        return closedBranch;
    }

    public void setClosedBranch(String closedBranch) {
        this.closedBranch = closedBranch;
    }

    public String getClosedTeacher() {
        return closedTeacher;
    }

    public void setClosedTeacher(String closedTeacher) {
        this.closedTeacher = closedTeacher;
    }
}
