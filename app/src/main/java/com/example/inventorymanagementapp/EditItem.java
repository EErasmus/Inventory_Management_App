package com.example.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class EditItem extends AppCompatActivity {
    UserHelper currentUser;
    EditText name, desc, price, quantity, alertQuantity;
    Button editItemBtn, deleteItemBtn;
    ImageView imgViewer;

    StorageReference mStorageRef;

    FirebaseDatabase rootNode;
    DatabaseReference refs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        name = findViewById(R.id.editTextTextPersonName2);
        desc = findViewById(R.id.editTextTextPersonName3);
        quantity = findViewById(R.id.editTextTextPersonName4);
        price = findViewById(R.id.editTextTextPersonName5);
        alertQuantity = findViewById(R.id.editTextTextPersonName6);
        editItemBtn = findViewById(R.id.editItemBtn);
        deleteItemBtn = findViewById(R.id.deleteItemBtn);
        imgViewer = findViewById(R.id.imageView3);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        rootNode = FirebaseDatabase.getInstance();

        currentUser = Utility.getAuthenticatedUser();
        InventoryHelper item = currentUser.inventory.get(getIntent().getStringExtra("key"));

        name.setText(item.name);
        desc.setText(item.description);
        quantity.setText(String.valueOf(item.quantity));
        alertQuantity.setText(String.valueOf(item.alertQuantity));
        price.setText(String.valueOf(item.price));

        String imageFileExt = item.id.split("_").length > 1 ?
                item.id.split("_")[1] : "";
        if (imageFileExt != "") {
            mStorageRef.child(item.id + "." + imageFileExt).
                    getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                        mImageView.setImageURI(uri);
                    try {
                        imgViewer.setImageBitmap(BitmapFactory.decodeStream(new URL(uri.toString()).openConnection().getInputStream()));
                    } catch (Exception e) {
                        String msg = e.getMessage();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    String message = item.id+"." + imageFileExt + e.getMessage();
                }
            });
        }

        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameVal, descVal;
                Integer quantityVal, alertQVal, priceVal;
                nameVal = name.getText().toString();
                descVal = desc.getText().toString();
                quantityVal = Integer.parseInt(quantity.getText().toString());
                alertQVal = Integer.parseInt(alertQuantity.getText().toString());
                priceVal = Integer.parseInt(price.getText().toString());
                InventoryHelper inventory = new InventoryHelper(nameVal, descVal, quantityVal, 1, alertQVal, priceVal,item.id);
                currentUser.inventory.put(item.id, inventory);

                rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/inventory")
                .child(item.id).setValue(inventory).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // Toast.makeText(getBaseContext(),"Edit successful");

                        }
                    }
                });

            }
        });

        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/inventory")
                .child(item.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            currentUser.inventory.remove(item.id);

                        }
                        else {
                            String msg = task.getException().getMessage();
                        }
                    }
                });
                Intent intent = new Intent(EditItem.this, MainContentActivity.class);
                startActivity(intent);
            }
        });
        }
    }
