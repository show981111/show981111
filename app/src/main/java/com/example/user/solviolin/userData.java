package com.example.user.solviolin;

public class userData {
    private String userID;
    private String userBranch;
    private String userDuration;
    private String userName;

    public userData(String userID, String userBranch, String userDuration, String userName) {
        this.userID = userID;
        this.userBranch = userBranch;
        this.userDuration = userDuration;
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserBranch() {
        return userBranch;
    }

    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }

    public String getUserDuration() {
        return userDuration;
    }

    public void setUserDuration(String userDuration) {
        this.userDuration = userDuration;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
