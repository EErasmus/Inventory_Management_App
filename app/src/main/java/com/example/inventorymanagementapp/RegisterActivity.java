package com.example.inventorymanagementapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class RegisterActivity extends AppCompatActivity {

    EditText userEmail, userPassword, userConfirmPassword, userContact;
    Button registerBtn;

    FirebaseDatabase rootNode;
    DatabaseReference refs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = findViewById(R.id.editTextTextPersonName);
        userPassword = findViewById(R.id.editTextTextPassword);
        userConfirmPassword = findViewById(R.id.editTextTextPassword2);
        userContact = findViewById(R.id.editTextTextPassword3);
        registerBtn = findViewById(R.id.button);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)  {
                rootNode = FirebaseDatabase.getInstance();
                refs = rootNode.getReference("users");

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
                String cipherText = null;
                String cipherEmail = null;
                try {
                    cipherText = Utility.encrypt(algorithm, userPassword.getText().toString(), key, ivParameterSpec);
                    cipherEmail = Utility.encrypt(algorithm, userEmail.getText().toString(), key, ivParameterSpec);
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
                // String plainText = Utility.decrypt(algorithm, cipherText, key, ivParameterSpec);

                UserHelper user = new UserHelper(userEmail.getText().toString(), userPassword.getText().toString(),userContact.getText().toString());
                refs.child(userContact.getText().toString()).setValue(user);
            }
        });
    }
}