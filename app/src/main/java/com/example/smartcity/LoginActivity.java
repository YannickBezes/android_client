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
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton menu_icon = findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        Button b_login = findViewById(R.id.button_login);

        b_login.setOnClickListener(v -> {
            if(checkNetworkConnection())
                login();
            else
                Toast.makeText(LoginActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });
    }

    public void login()
    {
        Log.d("login", "start");
        EditText login_username = findViewById(R.id.login_username);
        EditText login_password = findViewById(R.id.login_password);

        JSONObject json = new JSONObject();
        try {
            json.accumulate("username", login_username.getText().toString());
            json.accumulate("password", login_password.getText().toString());
        }
        catch (JSONException err) { }

        String url = "http://bsy.ovh:5000/login";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                response -> {
                    Log.d("res", response.toString());
                    try {
                        if (response.getBoolean("success")) {
                            EditText uname = findViewById(R.id.login_username);
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            sharedPreferences.edit().putString("username", uname.getText().toString()).apply();
                            sharedPreferences.edit().putString("user_token", response.getString("token")).apply();
                            Intent myIntent = new Intent(LoginActivity.this, ActualitiesActivity.class);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid username/password combination", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {}
                },
                error -> Log.d("Error.Response", error.toString())
        );

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


