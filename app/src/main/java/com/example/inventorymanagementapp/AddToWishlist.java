package com.example.inventorymanagementapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddToWishlist extends AppCompatActivity {

    EditText name, desc, price, quantity;
    Button addItemBtn;

    FirebaseDatabase rootNode;
    DatabaseReference refs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_wishlist);

        name = findViewById(R.id.editTextTextPersonName11);
        desc = findViewById(R.id.editTextTextPersonName12);
        quantity = findViewById(R.id.editTextTextPersonName13);
        price = findViewById(R.id.editTextTextPersonName14);
        addItemBtn = findViewById(R.id.loginBtnMainCont4);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                refs = rootNode.getReference("users/123456/wishlist");

                String nameVal, descVal;
                Integer quantityVal, alertQVal, priceVal;
                nameVal = name.getText().toString();
                descVal = desc.getText().toString();
                quantityVal = Integer.parseInt(quantity.getText().toString());
                priceVal = Integer.parseInt(price.getText().toString());
                String uniqueId = UUID.randomUUID().toString();
                InventoryHelper inventory = new InventoryHelper(nameVal, descVal, quantityVal, 2, 0, priceVal,uniqueId);
                refs.child(uniqueId).setValue(inventory);

                name.setText("");
                desc.setText("");
                quantity.setText("");
                price.setText("");
            }
        });
    }
}