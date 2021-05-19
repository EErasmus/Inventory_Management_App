package com.example.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.webkit.MimeTypeMap;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import android.net.Uri;

public class AddToWishlist extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText name, desc, price, quantity;
    Button addWishlistBtn;
    ImageView imgViewer;
    Uri imgUri;

    FirebaseDatabase rootNode;
    DatabaseReference refs;
    StorageReference mStorageRef;
    StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_wishlist);

        name = findViewById(R.id.editTextTextPersonName11);
        desc = findViewById(R.id.editTextTextPersonName12);
        quantity = findViewById(R.id.editTextTextPersonName13);
        price = findViewById(R.id.editTextTextPersonName14);
        addWishlistBtn = findViewById(R.id.loginBtnMainCont4);
        imgViewer = findViewById(R.id.imageView5);

        imgViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        addWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                refs = rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/wishlist");

                String nameVal, descVal;
                Integer quantityVal, priceVal;
                nameVal = name.getText().toString();
                descVal = desc.getText().toString();
                quantityVal = Integer.parseInt(quantity.getText().toString());
                priceVal = Integer.parseInt(price.getText().toString());
                String uniqueId = UUID.randomUUID().toString();
                uniqueId += imgUri != null ? "_" + getFileExtension(imgUri) : "";
                WishlistHelper wishlist = new WishlistHelper(nameVal, descVal, quantityVal, priceVal, uniqueId);
                refs.child(uniqueId).setValue(wishlist);
                Utility.getAuthenticatedUser().wishlist.put(wishlist.id, wishlist);
                if (imgUri != null) {
                    mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
                    StorageReference fileReference = mStorageRef.child(uniqueId + "." + getFileExtension(imgUri));
                    mUploadTask =  fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uploadPath = taskSnapshot.getUploadSessionUri().toString();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String ex = e.getMessage().toString();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                }

                name.setText("");
                desc.setText("");
                quantity.setText("");
                price.setText("");
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
            imgViewer.setImageURI(imgUri);
            // Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }
}