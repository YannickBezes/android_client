package com.example.smartcity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton menu_icon = findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        Button home_register = findViewById(R.id.home_register);
        Button home_login = findViewById(R.id.home_login);

        home_register.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        home_login.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }
}


