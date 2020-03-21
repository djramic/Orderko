package com.example.orderko;

public class User {
    private String username;
    private String table;
    private String password;
    private static User user;

    public static  User getInstance() {
        if(user == null)
            user = new User();
        return user;
    }

    public User() {
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

    public User(String username, String table, String password) {
        this.username = username;
        this.table = table;
        this.password = password;
    }
}
