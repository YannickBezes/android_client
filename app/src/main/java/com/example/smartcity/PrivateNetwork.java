package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateNetwork extends AppCompatActivity {

    private String network_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_network);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(PrivateNetwork.this, MenuActivity.class));
        });

        Bundle bundle = getIntent().getExtras();
        network_name = bundle.getString("network");

        TextView tv = findViewById(R.id.tv_private_network);
        tv.setText(network_name);

        Button b_join = findViewById(R.id.button_join_network);
        b_join.setOnClickListener(v -> {
            if(checkNetworkConnection())
                network_search();
            else
                Toast.makeText(PrivateNetwork.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });
    }

    public void network_search()
    {
        String url = "http://bsy.ovh:5000/network/";
        url += network_name + "/request";
        Log.d("url", url);
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(PrivateNetwork.this, "Votre demande a été enregistrée", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PrivateNetwork.this, NetworksActivity.class));
                        } else {
                            Toast.makeText(PrivateNetwork.this, "An error occured, please try again (later ?)", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {
                        Log.d("json", "error");}

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
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PrivateNetwork.this, NetworksActivity.class));
    }
}
