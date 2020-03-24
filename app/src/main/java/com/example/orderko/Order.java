package com.example.orderko;

public class Order {
    private String id;
    private String name;
    private String quantity;
    private String category;
    private String bulk;
    private String table;
    private String price;


    public Order(String id, String name,String category, String quantity, String bulk, String table, String price) {
        this.name = name;
        this.quantity = quantity;
        this.bulk = bulk;
        this.table = table;
        this.id = id;
        this.category = category;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
