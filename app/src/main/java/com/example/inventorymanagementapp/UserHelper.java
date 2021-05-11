package com.example.inventorymanagementapp;

import java.util.HashMap;
import java.util.List;

public class UserHelper {
    String name;

    public UserHelper(){}
    public UserHelper(String name, String password, String contact) {
        this.name = name;
        this.password = password;
        this.contactNumber = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String password) {
        this.contactNumber = contactNumber;
    }

    String password;
    String contactNumber;

    public HashMap<String, InventoryHelper> inventory, wishlist;

    public HashMap<String, InventoryHelper> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<String, InventoryHelper> inventory) {
        this.inventory = inventory;
    }

    public HashMap<String, InventoryHelper> getWishlist() {
        return wishlist;
    }

    public void setWishlist(HashMap<String, InventoryHelper> wishlist) {
        this.wishlist = wishlist;
    }
}
