package com.example.smartcity;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category_Holder extends RecyclerView.ViewHolder {

    private TextView name;
    private RecyclerView recyclerView;
    View view;

    public Category_Holder(View itemView)
    {
        super(itemView);
        view = itemView;

        recyclerView = itemView.findViewById(R.id.shops_category_card_recycler_view);
        name = itemView.findViewById(R.id.shop_card_category_name);
    }

    public void Bind(Category category)
    {
        name.setText(category.name);
        List<Shop> _shops = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        try {
            for (int i = 0; i < category.shops.length(); i++) {
                String name = category.shops.getJSONObject(i).getString("name");
                String address = category.shops.getJSONObject(i).getString("address");
                String city = category.shops.getJSONObject(i).getString("city");
                String cat = category.shops.getJSONObject(i).getJSONObject("category").getString("name");
                Boolean fav = category.shops.getJSONObject(i).getBoolean("is_fav");

                Shop s = new Shop(name, address, city, cat, fav);
                _shops.add(s);
            }
        } catch (JSONException err) {
            Log.d("json", "error");
        }

        recyclerView.setAdapter(new Shop_Adapter(_shops));

    }
}
