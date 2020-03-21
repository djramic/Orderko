package com.example.orderko;

public class Table {
    private String id;
    private String table_number;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Table(String id, String table_number, String password) {
        this.id = id;
        this.table_number = table_number;
        this.password = password;
    }
    public Table() {

    }
}
