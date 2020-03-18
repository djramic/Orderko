package com.example.orderko;

public class Order {
    private String name;
    private String quantity;

    public Order(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
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
