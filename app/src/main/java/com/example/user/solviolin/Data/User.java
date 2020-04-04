package com.example.user.solviolin.Data;

public class User {

    String userID;
    String userPassword;
    String userName;
    String userPhone;


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public User(String userID, String userPassword, String userName, String userPhone) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhone = userPhone;
    }
}
