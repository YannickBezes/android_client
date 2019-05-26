package com.example.smartcity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActualitiesActivity extends AppCompatActivity {

    private TabLayout.Tab last_selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualities);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(ActualitiesActivity.this, MenuActivity.class));
        });

        TabLayout tabLayout = findViewById(R.id.actualities_tab_layout);
        FrameLayout frameLayout = findViewById(R.id.actualitiesFrameLayout);

        TabLayout.Tab news_tab = tabLayout.newTab();
        news_tab.setIcon(R.drawable.news);
        tabLayout.addTab(news_tab, true);

        TabLayout.Tab weather_tab = tabLayout.newTab();
        weather_tab.setIcon(R.drawable.weather);
        tabLayout.addTab(weather_tab);

        TabLayout.Tab traffic_tab = tabLayout.newTab();
        traffic_tab.setIcon(R.drawable.traffic);
        tabLayout.addTab(traffic_tab);

        TabLayout.Tab calendar_tab = tabLayout.newTab();
        calendar_tab.setIcon(R.drawable.calendar);
        tabLayout.addTab(calendar_tab);

        TabLayout.Tab alarm_tab = tabLayout.newTab();
        alarm_tab.setIcon(R.drawable.alarm);
        tabLayout.addTab(alarm_tab);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.actualitiesFrameLayout, new ActualitiesNewsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment current_fragment = null;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (tab.getPosition()){
                    case 0:
                        last_selected = tab;
                        current_fragment = new ActualitiesNewsFragment();
                        ft.replace(R.id.actualitiesFrameLayout, current_fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                    case 1:
                        last_selected = tab;
                        current_fragment = new ActualitiesWeatherFragment();
                        ft.replace(R.id.actualitiesFrameLayout, current_fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                    case 2:
                        last_selected = tab;
                        current_fragment = new MapsActivity();
                        ft.replace(R.id.actualitiesFrameLayout, current_fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                    case 3:
                        last_selected = tab;
                        current_fragment = new ActualitiesCalendarFragment();
                        ft.replace(R.id.actualitiesFrameLayout, current_fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                    case 4:
                        startActivity(new Intent(AlarmClock.ACTION_SHOW_ALARMS));
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (last_selected != null)
            last_selected.select();
    }
}
