package com.example.orderko;

import java.util.ArrayList;

public class HistoryCard {
    private ArrayList<String> orders_list;
    private String date_and_time;
    private String club;

    public HistoryCard(ArrayList<String> orders_list, String date_and_time, String club) {
        this.orders_list = orders_list;
        this.date_and_time = date_and_time;
        this.club = club;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public ArrayList<String> getOrders_list() {
        return orders_list;
    }

    public void setOrders_list(ArrayList<String> orders_list) {
        this.orders_list = orders_list;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }
}

