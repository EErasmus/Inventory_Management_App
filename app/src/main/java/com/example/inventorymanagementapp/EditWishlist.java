package com.example.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class EditWishlist extends AppCompatActivity {
    UserHelper currentUser;
    EditText name, desc, price, quantity;
    Button editWishlistBtn, deleteWishlistBtn;
    ImageView imgViewer;

    StorageReference mStorageRef;

    FirebaseDatabase rootNode;
    DatabaseReference refs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wishlist);

        name = findViewById(R.id.editTextTextPersonName11);
        desc = findViewById(R.id.editTextTextPersonName12);
        quantity = findViewById(R.id.editTextTextPersonName13);
        price = findViewById(R.id.editTextTextPersonName14);
        editWishlistBtn = findViewById(R.id.editWishlistBtn);
        deleteWishlistBtn = findViewById(R.id.deleteWishlistBtn);
        imgViewer = findViewById(R.id.imageView5);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        rootNode = FirebaseDatabase.getInstance();

        currentUser = Utility.getAuthenticatedUser();
        WishlistHelper item = currentUser.wishlist.get(getIntent().getStringExtra("key"));

        name.setText(item.name);
        desc.setText(item.description);
        quantity.setText(String.valueOf(item.quantity));
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

        editWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameVal, descVal;
                Integer quantityVal, priceVal;
                nameVal = name.getText().toString();
                descVal = desc.getText().toString();
                quantityVal = Integer.parseInt(quantity.getText().toString());
                priceVal = Integer.parseInt(price.getText().toString());
                WishlistHelper wishlist = new WishlistHelper(nameVal, descVal, quantityVal, priceVal,item.id);
                currentUser.wishlist.put(item.id, wishlist);

                rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/wishlist")
                        .child(item.id).setValue(wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // Toast.makeText(getBaseContext(),"Edit successful");

                        }
                    }
                });
                Intent intent = new Intent(EditWishlist.this, MainContentActivity.class);
                startActivity(intent);
            }
        });

        deleteWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/wishlist")
                        .child(item.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            currentUser.wishlist.remove(item.id);

                        }
                        else {
                            String msg = task.getException().getMessage();
                        }
                    }
                });
                Intent intent = new Intent(EditWishlist.this, MainContentActivity.class);
                startActivity(intent);
            }
        });

    }


}