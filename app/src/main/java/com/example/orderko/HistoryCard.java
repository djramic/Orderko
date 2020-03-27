package com.example.orderko;

import java.util.ArrayList;

public class HistoryCard {
    private ArrayList<String> ordersList;
    private String date_and_time;
    private String club;
    private String sum;

    public HistoryCard(ArrayList<String> ordersList, String date_and_time, String club, String sum) {
        this.ordersList = new ArrayList<>(ordersList);
        this.date_and_time = date_and_time;
        this.club = club;
        this.sum = sum;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public ArrayList<String> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(ArrayList<String> ordersList) {
        this.ordersList = new ArrayList<>(ordersList);;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }
}

