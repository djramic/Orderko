package com.example.orderko;

public class User {
    private String username;
    private String table;
    private static User user;

    public static  User getInstance() {
        if(user == null)
            user = new User();
        return user;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public User(String username, String table) {
        this.username = username;
        this.table = table;
    }
}
