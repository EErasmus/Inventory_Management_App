package com.example.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainContentActivity extends AppCompatActivity {

    FloatingActionButton mainActionBtn;
    private boolean ifWishlistScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentInventory()).commit();
        }

        mainActionBtn = findViewById(R.id.mainActionBtn);

        mainActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainContentActivity.this, AddItem.class);
                if (ifWishlistScreen) {
                    intent = new Intent(MainContentActivity.this, AddToWishlist.class);
                }
                startActivity(intent);
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.fragmentWishlist:
                            selectedFragment = new FragmentWishlist();
                            ifWishlistScreen = true;
                            break;
                        case R.id.fragmentInventory:
                            selectedFragment = new FragmentInventory();
                            ifWishlistScreen = false;
                            break;
                        case R.id.fragmentAlerts:
                            selectedFragment = new FragmentAlerts();
                            break;
                        case R.id.fragmentProfile:
                            selectedFragment = new FragmentProfile();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();

                    return true;
                }
            };
}