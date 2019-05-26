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
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsToNetwork extends AppCompatActivity {

    private String network_name;
    static RecyclerView recyclerView;
    static Request_Join_Network_Adapter request_join_network_adapter;
    static List<Request_Join_Network> _rjn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_to_network);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(RequestsToNetwork.this, MenuActivity.class));
        });

        Bundle bundle = getIntent().getExtras();
        network_name = bundle.getString("network_name");

        if(checkNetworkConnection()){
            Log.d("getreq", "pre");
            get_requests();
        }
        else
        {
            Toast.makeText(RequestsToNetwork.this, "Not Connected!", Toast.LENGTH_SHORT).show();
        }
    }


    public void get_requests()
    {
        String url = "http://bsy.ovh:5000/network/";
        url += network_name + "/requests";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            recyclerView = (RecyclerView) findViewById(R.id.requests_to_private_network_recycler);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                            Log.d("response", response.toString());
                            if (response.getJSONArray("sub_requests").length() == 0)
                            {
                                _rjn.add(new Request_Join_Network("Il n'y a aucune demande"));
                            }
                            else {
                                for (int i = 0; i < response.getJSONArray("sub_requests").length(); i++) {
                                    String username = response.getJSONArray("sub_requests").getString(i);

                                    Request_Join_Network rjn = new Request_Join_Network(username);
                                    _rjn.add(rjn);
                                }
                            }
                            request_join_network_adapter = new Request_Join_Network_Adapter(_rjn, network_name);
                            recyclerView.setAdapter(request_join_network_adapter);
                        } else {
                            Toast.makeText(RequestsToNetwork.this, "Error during requests loading...", Toast.LENGTH_SHORT).show();
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

    public static void remove_from_list(int position){
        _rjn.remove(position);
        recyclerView.removeViewAt(position);
        request_join_network_adapter.notifyItemRemoved(position);
        request_join_network_adapter.notifyItemRangeChanged(position, _rjn.size());

        request_join_network_adapter.notifyDataSetChanged();
    }
}
