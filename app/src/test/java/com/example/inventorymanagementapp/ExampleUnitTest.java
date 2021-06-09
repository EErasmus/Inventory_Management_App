package com.example.inventorymanagementapp;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void retuned_user_is_what_we_saved(){
        String username = "test";
        String password = "password";
        String contact = "99999";
        UserHelper user = new UserHelper(username,password,contact);
        Utility.setAuthenticatedUser(user);
        assertEquals(Utility.getAuthenticatedUser().name, username);
        assertEquals(Utility.getAuthenticatedUser().password, password);
        assertEquals(Utility.getAuthenticatedUser().contactNumber, contact);
    }
    @Test
    public void inventory_added_successfully(){
        String name = "item", description="desc";
        Integer quantity = 1;
        Integer type = 1;
        String uniqueId = UUID.randomUUID().toString();
        InventoryHelper inventory = new InventoryHelper(name, description, quantity, type, quantity, quantity,uniqueId);
        String username = "test";
        String password = "password";
        String contact = "99999";
        UserHelper user = new UserHelper(username,password,contact);
        Utility.setAuthenticatedUser(user);
        Utility.getAuthenticatedUser().inventory.put(inventory.id, inventory);
        UserHelper userTest = Utility.getAuthenticatedUser();
        assertEquals(userTest.inventory.get(uniqueId), inventory);
    }
}