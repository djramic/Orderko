package com.example.orderko;

public class Order {
    private String name;
    private String quantity;
    private String bulk;
    private String table;


    public Order(String name, String quantity, String bulk, String table) {
        this.name = name;
        this.quantity = quantity;
        this.bulk = bulk;
        this.table = table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public String getBulk() {
        return bulk;
    }

    public void setBulk(String bulk) {
        this.bulk = bulk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
}
