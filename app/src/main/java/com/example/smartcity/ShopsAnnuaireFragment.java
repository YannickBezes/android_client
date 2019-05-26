package com.example.smartcity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

public class ShopsAnnuaireFragment extends Fragment {

    public ShopsAnnuaireFragment(){}

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
        return inflater.inflate(R.layout.shops_annuaire_fragment, container, false);
    }

    public void get_shops() {
        List<Category> _categories = new ArrayList<>();
        String url = "http://bsy.ovh:5000/categories";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Log.d("response_shops", response.toString());
                            RecyclerView recyclerView = getActivity().findViewById(R.id.ShopsAnnuaireFragmentRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            JSONArray categories = response.getJSONArray("categories");
                            for (int i = 0; i < categories.length(); i++) {
                                String name = categories.getJSONObject(i).getString("name");
                                JSONArray shops = categories.getJSONObject(i).getJSONArray("shops");

                                Category c = new Category(name, shops);

                                _categories.add(c);
                            }

                            recyclerView.setAdapter(new Category_Adapter(_categories));

                        } else {
                            Toast.makeText(getActivity(), "Can't find any category", Toast.LENGTH_SHORT).show();
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
