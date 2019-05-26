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
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class NetworksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(NetworksActivity.this, MenuActivity.class));
        });

        get_subscribed_networks();

        Button b_search = findViewById(R.id.button_network_search);

        b_search.setOnClickListener(v -> {
            if(checkNetworkConnection())
                network_search();
            else
                Toast.makeText(NetworksActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });

        Button b_create_network = findViewById(R.id.create_network_button);
        b_create_network.setOnClickListener(v -> {
            if(checkNetworkConnection()){
                startActivity(new Intent(NetworksActivity.this, Create_Network.class));
            }
            else
            {
                Toast.makeText(NetworksActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void network_search()
    {
        EditText network = findViewById(R.id.searched_network);

        String url = "http://bsy.ovh:5000/network/";
        url += network.getText().toString();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject json = response.getJSONObject("data");
                            Bundle bundle = new Bundle();
                            bundle.putString("network", json.toString());
                            Intent myIntent = new Intent(NetworksActivity.this, DisplayNetwork.class);
                            myIntent.putExtras(bundle);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(NetworksActivity.this, "Can't find any network with this name", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {Log.d("json", "error");}
                },
                error -> Log.d("Error.Response", error.toString())
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                return headers;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(req);
    }

    public void get_subscribed_networks()
    {
        String url = "http://bsy.ovh:5000/network";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            List<Subscribed_Network> _sns = new ArrayList<>();
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.subscribed_networks_recycler);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                            if (response.getJSONArray("networks").length() == 0)
                            {
                                _sns.add(new Subscribed_Network("Vous ne vous êtes abonné à aucun réseau"));
                            }
                            else {
                                for (int i = 0; i < response.getJSONArray("networks").length(); i++) {
                                    String nom = response.getJSONArray("networks").getJSONObject(i).getString("name");

                                    Subscribed_Network sn = new Subscribed_Network(nom);
                                    _sns.add(sn);
                                }
                            }
                            recyclerView.setAdapter(new Subscribed_Networks_Adapter(_sns));
                        } else {
                            Toast.makeText(NetworksActivity.this, "Error during the loading of your networks", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {Log.d("json", "error");}

                },
                error -> Log.d("Error.Response", error.toString())
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
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
}
