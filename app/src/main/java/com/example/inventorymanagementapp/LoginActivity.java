package com.example.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class LoginActivity extends AppCompatActivity {

    TextView registerTextView;
    Button loginButtonMainCont;
    EditText userEmail, userPassword;
    FirebaseDatabase rootNode;
    DatabaseReference refs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.editTextTextPersonName);
        userPassword = findViewById(R.id.editTextTextPassword);
        final String[] cipherText = {null};
        final String[] cipherEmail = {null};

        registerTextView = findViewById(R.id.lgnregisterTxtView);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButtonMainCont = findViewById(R.id.loginBtnMainCont);

        loginButtonMainCont.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                refs = rootNode.getReference("users");

                String username = userEmail.getText().toString(), userpwd = userPassword.getText().toString();
                if (username.isEmpty()){
                    userEmail.setError("Field can not be blank");
                    return;
                } else {
                    userEmail.setError(null);
                }
                if (userpwd.isEmpty()){
                    userPassword.setError("Field can not be blank");
                    return;
                } else {
                    userPassword.setError(null);
                }

                SecretKey key = null;
                try {
                    String password = "baeldung";
                    String salt = "12345678";
                    key = Utility.getKeyFromPassword(password,salt);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                IvParameterSpec ivParameterSpec = Utility.generateIv();
                String algorithm = "AES/CBC/PKCS5Padding";

                try {
                    cipherText[0] = Utility.encrypt(algorithm, userpwd, key, ivParameterSpec);
                    cipherEmail[0] = Utility.encrypt(algorithm, username, key, ivParameterSpec);
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                rootNode = FirebaseDatabase.getInstance();
                refs = rootNode.getReference("users");
                Query checkUser = refs.orderByChild("contactNumber").equalTo(username);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String passwordFromDB = dataSnapshot.child(username).child("password").getValue(String.class);
                            UserHelper user = dataSnapshot.child(username).getValue(UserHelper.class);
                            user.contactNumber = username;
                            Utility.setAuthenticatedUser(user);
                            if(passwordFromDB.equals(userpwd)){
                                Intent intent = new Intent(LoginActivity.this, MainContentActivity.class);
                                startActivity(intent);
                            }
                        } else{
                            userPassword.setError("Incorrect details");
                            userEmail.setError("Incorrect details");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }
}