package com.example.inventorymanagementapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import android.net.Uri;

public class AddItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText name, desc, price, quantity, alertQuantity;
    Button addItemBtn;
    ImageView imgViewer;
    Uri imgUri;

    FirebaseDatabase rootNode;
    DatabaseReference refs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = findViewById(R.id.editTextTextPersonName2);
        desc = findViewById(R.id.editTextTextPersonName3);
        quantity = findViewById(R.id.editTextTextPersonName4);
        price = findViewById(R.id.editTextTextPersonName5);
        alertQuantity = findViewById(R.id.editTextTextPersonName6);
        addItemBtn = findViewById(R.id.loginBtnMainCont2);
        imgViewer = findViewById(R.id.imageView3);

        imgViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                refs = rootNode.getReference("users/123456/inventory");

                String nameVal, descVal;
                Integer quantityVal, alertQVal, priceVal;
                nameVal = name.getText().toString();
                descVal = desc.getText().toString();
                quantityVal = Integer.parseInt(quantity.getText().toString());
                alertQVal = Integer.parseInt(alertQuantity.getText().toString());
                priceVal = Integer.parseInt(price.getText().toString());
                String uniqueId = UUID.randomUUID().toString();
                InventoryHelper inventory = new InventoryHelper(nameVal, descVal, quantityVal, 1, alertQVal, priceVal,uniqueId);
                refs.child(uniqueId).setValue(inventory);

                name.setText("");
                desc.setText("");
                quantity.setText("");
                alertQuantity.setText("");
                price.setText("");
            }
            });

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgUri = data.getData();
           // Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }
}