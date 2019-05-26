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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopsInterestsFragment extends Fragment {

    public ShopsInterestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (checkNetworkConnection()) {
            get_shops();
        } else
            Toast.makeText(getActivity(), "Not Connected!", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.shops_interests_fragment, container, false);
    }

    public void get_shops() {
        List<Shop> _shops = new ArrayList<>();
        String url = "http://bsy.ovh:5000/shops/interest";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Log.d("response_shops", response.toString());
                            RecyclerView recyclerView = getActivity().findViewById(R.id.ShopsInterestsFragmentRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            JSONArray shops = response.getJSONArray("shops");
                            for (int i = 0; i < shops.length(); i++) {
                                String name = shops.getJSONObject(i).getString("name");
                                String address = shops.getJSONObject(i).getString("address");
                                String city = shops.getJSONObject(i).getString("city");
                                String category = shops.getJSONObject(i).getJSONObject("category").getString("name");
                                Boolean fav = shops.getJSONObject(i).getBoolean("is_fav");

                                Shop s = new Shop(name, address, city, category, fav);

                                _shops.add(s);
                            }

                            recyclerView.setAdapter(new Shop_Adapter(_shops));

                        } else {
                            Toast.makeText(getActivity(), "Can't find any shops for you", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException err) {
                        Log.d("json", "error");
                    }
                },
                error -> Log.d("Error.Response", error.toString())
        ) {
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