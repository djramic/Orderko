package com.example.orderko;

import java.io.Serializable;

public class Drink implements Serializable {
    private String name;
    private String category;
    private String bulk;

    public Drink(String name, String category, String bulk) {
        this.name = name;
        this.category = category;
        this.bulk = bulk;
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
