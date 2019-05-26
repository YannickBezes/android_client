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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adds);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(AddsActivity.this, MenuActivity.class));
        });

        if(checkNetworkConnection()) {
            get_adds();
        }
        else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
    }

    public void get_adds()
    {
        String url = "http://bsy.ovh:5000/pubs";

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Log.d("response_adds", response.toString());
                            JSONArray adds = response.getJSONArray("pubs");

                            List<Add> _adds = new ArrayList<>();
                            RecyclerView recyclerView = findViewById(R.id.adds_recycler_view);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));

                            for(int i=0; i < adds.length(); i++)
                            {
                                String img = adds.getJSONObject(i).getString("image");
                                Add a = new Add(img);
                                _adds.add(a);
                            }

                            recyclerView.setAdapter(new Add_Adapter(_adds));
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.no_add), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {Log.d("json", "error");}
                },
                error -> Log.d("Error.Response", error.toString())
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                return headers;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(req);
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // connected

        } else {
            // not connected
            Log.e("internet", "no connection");
        }

        return isConnected;
    }
}
