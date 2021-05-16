package com.example.inventorymanagementapp;

import java.util.UUID;

public class WishlistHelper {

    String name, description, id;
    Integer quantity;
    Integer type;

    public WishlistHelper(){}


    public WishlistHelper(String name, String description, Integer quantity, Integer type, Integer alertQuantity, Integer price, String id) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.type = type;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    Integer price;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public WishlistHelper(String name, String description, Integer quantity, Integer price, String id) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.type = 1;
        this.id = id;
    }
}
