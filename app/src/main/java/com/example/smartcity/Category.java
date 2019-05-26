package com.example.smartcity;

import org.json.JSONArray;

public class Category {

    public String name;
    public JSONArray shops;

    public Category(String name, JSONArray shops) {
        this.name = name;
        this.shops = shops;
    }
}
