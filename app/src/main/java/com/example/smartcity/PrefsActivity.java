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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrefsActivity extends AppCompatActivity {

    EditText et_firstname;
    EditText et_lastname;
    EditText et_username;
    EditText et_email;
    EditText et_password;
    Spinner spinner_gender;
    EditText et_size;
    EditText et_weight;
    EditText et_city;
    EditText et_interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(PrefsActivity.this, MenuActivity.class));
        });

        et_firstname = findViewById(R.id.prefs_firstname);
        et_lastname = findViewById(R.id.prefs_lastname);
        et_username = findViewById(R.id.prefs_username);
        et_email = findViewById(R.id.prefs_email);
        et_password = findViewById(R.id.prefs_password);
        spinner_gender = findViewById(R.id.prefs_gender);
        et_size = findViewById(R.id.prefs_size);
        et_weight = findViewById(R.id.prefs_weight);
        et_city = findViewById(R.id.prefs_city);
        et_interests = findViewById(R.id.prefs_interests);

        if(checkNetworkConnection())
            get_user_prefs();
        else
            Toast.makeText(PrefsActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();

        Button button_change_prefs = findViewById(R.id.button_change_prefs);
        button_change_prefs.setOnClickListener(v -> {
            if(checkNetworkConnection())
                change_prefs();
            else
                Toast.makeText(PrefsActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });
    }

    public void get_user_prefs()
    {
        String url = "http://bsy.ovh:5000/user/";
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        url += sharedPreferences.getString("username", null);
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject json = response.getJSONObject("data");
                            Log.d("json", json.toString());
                            et_firstname.setText(json.getString("firstname"));
                            et_lastname.setText(json.getString("lastname"));
                            et_username.setText(json.getString("username"));
                            et_email.setText(json.getString("email"));
                            et_password.setText("");
                            if (json.getString("gender").equals("Homme"))
                                spinner_gender.setSelection(0);
                            else
                                spinner_gender.setSelection(1);
                            et_size.setText(json.getString("height"));
                            et_weight.setText(json.getString("weight"));
                            et_city.setText(json.getString("city"));
                            et_interests.setText(json.getString("interest"));
                        } else {
                            Toast.makeText(PrefsActivity.this, "Can't retrieve your data", Toast.LENGTH_SHORT).show();
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

    public void change_prefs()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        JSONObject json = new JSONObject();
        try {
            json.accumulate("firstname", et_firstname.getText().toString());
            json.accumulate("lastname", et_lastname.getText().toString());
            json.accumulate("username", et_username.getText().toString());
            json.accumulate("email", et_email.getText().toString());
            json.accumulate("password", et_password.getText().toString());
            json.accumulate("gender", spinner_gender.getSelectedItem().toString());
            json.accumulate("height", et_size.getText().toString());
            json.accumulate("weight", et_weight.getText().toString());
            json.accumulate("lat", location.getLatitude());
            json.accumulate("lng", location.getLongitude());
            json.accumulate("city", et_city.getText().toString());
            json.accumulate("interest", et_interests.getText().toString());
        }
        catch (JSONException err) {}

        String url = "http://bsy.ovh:5000/user";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                json,
                response -> {
                    Log.d("res", response.toString());
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(PrefsActivity.this, getResources().getString(R.string.prefs_changed), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(PrefsActivity.this, PrefsActivity.class);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(PrefsActivity.this, "Invalid data, please try again", Toast.LENGTH_SHORT).show();
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
