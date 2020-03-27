package com.example.orderko;

public class HistoryOrder {
    private String drink;
    private String category;
    private String bulk;
    private String quantity;
    private String price;
    private String table_number;
    private String club;
    private String time_date;


    public HistoryOrder(String drink, String category, String bulk, String quantity, String price, String table_number, String club, String time_date) {
        this.drink = drink;
        this.category = category;
        this.bulk = bulk;
        this.quantity = quantity;
        this.price = price;
        this.table_number = table_number;
        this.club = club;
        this.time_date = time_date;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBulk() {
        return bulk;
    }

    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getTime_date() {
        return time_date;
    }

    public void setTime_date(String time_date) {
        this.time_date = time_date;
    }
}
