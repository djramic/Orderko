package com.example.orderko;

import java.io.Serializable;

public class Drink implements Serializable {
    private String id;
    private String name;
    private String category;
    private String bulk;
    private String quantity;
    private String price;

    public Drink(String id, String name, String category, String bulk, String quantity, String price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.bulk = bulk;
        this.quantity = quantity;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
