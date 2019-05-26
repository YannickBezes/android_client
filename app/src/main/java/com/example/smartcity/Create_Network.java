package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.util.HashMap;
import java.util.Map;

public class Create_Network extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__network);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(Create_Network.this, MenuActivity.class));
        });

        Button b_create_network = findViewById(R.id.button_create_network);
        b_create_network.setOnClickListener(v -> {
            if(checkNetworkConnection())
                createNetwork();
            else
                Toast.makeText(Create_Network.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });

        Button b_cancel = findViewById(R.id.button_cancel_network);
        b_cancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void createNetwork() {
        Log.d("login", "start");
        EditText network_name = findViewById(R.id.et_create_network);

        SwitchCompat public_switch = findViewById(R.id.public_switch);

        JSONObject json = new JSONObject();
        try {
            json.accumulate("name", network_name.getText().toString());
            json.accumulate("public", public_switch.isChecked());
        }
        catch (JSONException err) { }

        String url = "http://bsy.ovh:5000/network";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                response -> {
                    Log.d("res", response.toString());
                    try {
                        if (response.getBoolean("success")) {
                            Intent myIntent = new Intent(Create_Network.this, NetworksActivity.class);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(Create_Network.this, "Invalid username/password combination", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {}
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
}
