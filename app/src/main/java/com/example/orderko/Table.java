package com.example.orderko;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String id;
    private String table_number;
    private String password;
    private String users_num;
    private String table_bill;

    public String getTable_bill() {
        return table_bill;
    }

    public void setTable_bill(String table_bill) {
        this.table_bill = table_bill;
    }

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

    public String getUsers_num() {
        return users_num;
    }

    public void setUsers_num(String users_num) {
        this.users_num = users_num;
    }

    public Table(String id, String table_number, String password, String users_num, String table_bill) {
        this.id = id;
        this.table_number = table_number;
        this.password = password;
        this.users_num = users_num;
        this.table_bill = table_bill;
    }

    public Table() {

    }
}
