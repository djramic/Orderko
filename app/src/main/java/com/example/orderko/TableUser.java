package com.example.orderko;

public class TableUser {
    String userId;
    String userEmail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public TableUser(String userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public TableUser() {

    }

}
