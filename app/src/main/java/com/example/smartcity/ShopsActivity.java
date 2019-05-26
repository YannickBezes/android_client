package com.example.smartcity;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ShopsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(ShopsActivity.this, MenuActivity.class));
        });

        TabLayout tabLayout = findViewById(R.id.shops_tab_layout);
        FrameLayout frameLayout = findViewById(R.id.shopsFrameLayout);

        TabLayout.Tab annuaire_tab = tabLayout.newTab();
        annuaire_tab.setIcon(R.drawable.annuaire);
        tabLayout.addTab(annuaire_tab, true);

        TabLayout.Tab proximity_tab = tabLayout.newTab();
        proximity_tab.setIcon(R.drawable.proximity2);
        tabLayout.addTab(proximity_tab);

        TabLayout.Tab interests_tab = tabLayout.newTab();
        interests_tab.setIcon(R.drawable.interests);
        tabLayout.addTab(interests_tab);

        TabLayout.Tab fav_tab = tabLayout.newTab();
        fav_tab.setIcon(R.drawable.fav);
        tabLayout.addTab(fav_tab);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.shopsFrameLayout, new ShopsAnnuaireFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment current_fragment = null;
                switch (tab.getPosition()){
                    case 0:
                        current_fragment = new ShopsAnnuaireFragment();
                        break;
                    case 1:
                        current_fragment = new ShopsProximityFragment();
                        break;
                    case 2:
                        current_fragment = new ShopsInterestsFragment();
                        break;
                    case 3:
                        current_fragment = new ShopsFavFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.shopsFrameLayout, current_fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
