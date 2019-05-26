package com.example.smartcity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActualitiesWeatherFragment extends Fragment {

    public ActualitiesWeatherFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(checkNetworkConnection()) {
            get_weather();
        }
        else
            Toast.makeText(getActivity(), "Not Connected!", Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.weather_fragment, container, false);
    }

    public void get_weather()
    {
        String url = "http://bsy.ovh:5000/weather?lat=";
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        url += location.getLatitude() + "&lng=" + location.getLongitude();

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Log.d("response_weather", response.toString());
                            JSONObject weather = response.getJSONObject("weather");

                            TextView tv_city = getActivity().findViewById(R.id.weather_frag_city);
                            tv_city.setText(weather.getString("name"));

                            TextView tv_weather = getActivity().findViewById(R.id.weather_frag_meteo);
                            tv_weather.setText(getResources().getString(R.string.weather) + " : " + weather.getString("description"));

                            TextView tv_temp = getActivity().findViewById(R.id.weather_frag_temperature);
                            tv_temp.setText(getResources().getString(R.string.temperature) + " : " + weather.getString("temp") + "°C");

                            TextView tv_humidity = getActivity().findViewById(R.id.weather_frag_humidity);
                            tv_humidity.setText(getResources().getString(R.string.humdity) + " : " + weather.getString("humidity") + "%");

                            TextView tv_wind = getActivity().findViewById(R.id.weather_frag_wind);
                            tv_wind.setText(getResources().getString(R.string.wind) + " : " + weather.getJSONObject("wind").getString("speed") + " m/s, " + weather.getJSONObject("wind").getString("deg") + "°");

                            TextView tv_clouds = getActivity().findViewById(R.id.weather_frag_clouds);
                            tv_clouds.setText(getResources().getString(R.string.cloouds) + " : " + weather.getString("clouds") + "%");
                        } else {
                            Toast.makeText(getActivity(), "Can't find any network with this name", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {Log.d("json", "error");}
                },
                error -> Log.d("Error.Response", error.toString())
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE);
                headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                return headers;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(req);
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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
