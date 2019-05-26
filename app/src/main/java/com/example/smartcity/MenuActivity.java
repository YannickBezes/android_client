package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            onBackPressed();
        });

        Button b_actualities = findViewById(R.id.menu_actuality);
        Button b_shops = findViewById(R.id.menu_shops);
        Button b_networks = findViewById(R.id.menu_networks);
        Button b_adds = findViewById(R.id.menu_adds);
        Button b_prefs = findViewById(R.id.menu_prefs);

        b_actualities.setOnClickListener((v) -> {
            startActivity(new Intent(MenuActivity.this, ActualitiesActivity.class));
        });

        b_shops.setOnClickListener((v) -> {
            startActivity(new Intent(MenuActivity.this, ShopsActivity.class));
        });

        b_networks.setOnClickListener((v) -> {
            startActivity(new Intent(MenuActivity.this, NetworksActivity.class));
        });

        b_adds.setOnClickListener((v) -> {
            startActivity(new Intent(MenuActivity.this, AddsActivity.class));
        });

        b_prefs.setOnClickListener((v) -> {
            startActivity(new Intent(MenuActivity.this, PrefsActivity.class));
        });
    }
}

