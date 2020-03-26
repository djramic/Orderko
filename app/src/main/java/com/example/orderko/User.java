package com.example.orderko;

public class User {
    private String table;
    private String password;
    private String userBill;
    private String userLastBill;
    private String club;
    private String tableBill;
    private String usersNum;
    private static User user;

    public static  User getInstance() {
        if(user == null)
            user = new User();
        return user;
    }

    public String getUsersNum() {
        return usersNum;
    }

    public void setUsersNum(String usersNum) {
        this.usersNum = usersNum;
    }

    public String getTableBill() {
        return tableBill;
    }

    public void setTableBill(String tableBill) {
        this.tableBill = tableBill;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public User() {
    }

    public String getUserLastBill() {
        return userLastBill;
    }

    public void setUserLastBill(String userLastBill) {
        this.userLastBill = userLastBill;
    }

    public String getUserBill() {
        return userBill;
    }

    public void setUserBill(String userBill) {
        this.userBill = userBill;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }


    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public User(String table, String password) {
        this.table = table;
        this.password = password;
    }
}
