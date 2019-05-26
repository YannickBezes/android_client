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
import android.widget.Spinner;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton menu_icon = findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        Button b_register = findViewById(R.id.button_register);

        b_register.setOnClickListener(v -> {
            if(checkNetworkConnection())
                register();
            else
                Toast.makeText(RegisterActivity.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        });
    }

    public void register()
    {
        EditText et_firstname = findViewById(R.id.firstname);
        EditText et_lastname = findViewById(R.id.lastname);
        EditText et_username = findViewById(R.id.username);
        EditText et_email = findViewById(R.id.email);
        EditText et_password = findViewById(R.id.password);
        Spinner spinner_gender = findViewById(R.id.gender);
        EditText et_size = findViewById(R.id.size);
        EditText et_weight = findViewById(R.id.weight);
        EditText et_city = findViewById(R.id.city);
        EditText et_interests = findViewById(R.id.interests);

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
                Request.Method.POST,
                url,
                json,
                response -> {
                    Log.d("res", response.toString());
                    try {
                        if (response.getBoolean("success")) {
                            Log.d("change activity" ,"start");
                            EditText uname = findViewById(R.id.username);
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            sharedPreferences.edit().putString("username", uname.getText().toString()).apply();
                            sharedPreferences.edit().putString("user_token", response.getString("token")).apply();
                            Intent myIntent = new Intent(RegisterActivity.this, ActualitiesActivity.class);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Invalid data, please try again", Toast.LENGTH_SHORT).show();
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


