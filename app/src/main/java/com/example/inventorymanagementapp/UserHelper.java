package com.example.inventorymanagementapp;

public class UserHelper {
    String name;

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
}
