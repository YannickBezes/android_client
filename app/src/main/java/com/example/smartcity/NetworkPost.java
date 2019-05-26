package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkPost extends AppCompatActivity {

    String network_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_post);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(NetworkPost.this, MenuActivity.class));
        });

        Bundle bundle = getIntent().getExtras();
        network_name = bundle.getString("name");

        Button post = findViewById(R.id.button_post);

        post.setOnClickListener(v -> {
            if(checkNetworkConnection())
                post();
            else
                Toast.makeText(NetworkPost.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });
    }

    public void post()
    {
        EditText et_message = findViewById(R.id.message_to_post);
        String message = et_message.getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.accumulate("content", message);
        }
        catch (JSONException err) { }

        String url = "http://bsy.ovh:5000/network/";
        url += network_name;
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject res = response.getJSONObject("network");
                            Bundle bundle = new Bundle();
                            bundle.putString("network", res.toString());
                            Intent myIntent = new Intent(NetworkPost.this, DisplayNetwork.class);
                            myIntent.putExtras(bundle);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(NetworkPost.this, "Invalid username/password combination", Toast.LENGTH_SHORT).show();
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
