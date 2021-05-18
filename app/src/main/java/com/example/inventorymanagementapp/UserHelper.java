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
        this.inventory = new HashMap<String, InventoryHelper>();
        this.wishlist = new HashMap<String, WishlistHelper>();

        this.inventory.put("01d97e60-67ba-46dc-8f13-da35ffa03314", new InventoryHelper("1","1",1,1,1,1,"1"));
        this.wishlist.put("01d97e60-67ba-46dc-8f13-da35ffa03314", new WishlistHelper("1","1",1,1,1,1,"1"));

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

    public HashMap<String, InventoryHelper> inventory;
    public HashMap<String, WishlistHelper> wishlist;

    public HashMap<String, InventoryHelper> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<String, InventoryHelper> inventory) {
        this.inventory = inventory;
    }

    public HashMap<String, WishlistHelper> getWishlist() {
        return wishlist;
    }

    public void setWishlist(HashMap<String, WishlistHelper> wishlist) {
        this.wishlist = wishlist;
    }
}
