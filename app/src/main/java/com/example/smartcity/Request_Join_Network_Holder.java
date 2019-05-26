package com.example.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request_Join_Network_Holder extends RecyclerView.ViewHolder {

    private TextView username;
    private ImageButton accept;
    private ImageButton reject;

    public Request_Join_Network_Holder(View itemView, String network)
    {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.rjn_card_username);
        accept = itemView.findViewById(R.id.rjn_card_accept);
        reject = itemView.findViewById(R.id.rjn_card_reject);

        if (username.getText().toString().equals("Il n'y a aucune demande"))
        {
            accept.setVisibility(itemView.INVISIBLE);
            reject.setVisibility(itemView.INVISIBLE);
        }

        accept.setOnClickListener(v -> {
            String url = "http://bsy.ovh:5000/network/";
            url += network + "/accept/" + username.getText().toString();
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    null,
                    response -> {
                        try {
                            if (response.getBoolean("success")) {
                                int position = -1;
                                for(Request_Join_Network r : RequestsToNetwork._rjn)
                                {
                                    if(r.getUsername().equals(username.getText().toString()))
                                        position = RequestsToNetwork._rjn.indexOf(r);
                                }
                                RequestsToNetwork.remove_from_list(position);
                            } else {
                                Toast.makeText(itemView.getContext(), "Error while accepting", Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("user_prefs", itemView.getContext().MODE_PRIVATE);
                    headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                    return headers;
                }
            };

            RequestHandler.getInstance(itemView.getContext()).addToRequestQueue(req);
        });

        reject.setOnClickListener(v -> {
            String url = "http://bsy.ovh:5000/network/";
            url += network + "/reject/" + username.getText().toString();
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    null,
                    response -> {
                        try {
                            if (response.getBoolean("success")) {
                                int position = -1;
                                for(Request_Join_Network r : RequestsToNetwork._rjn)
                                {
                                    if(r.getUsername().equals(username.getText().toString()))
                                        position = RequestsToNetwork._rjn.indexOf(r);
                                }
                                RequestsToNetwork.remove_from_list(position);
                            } else {
                                Toast.makeText(itemView.getContext(), "Error while rejecting", Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("user_prefs", itemView.getContext().MODE_PRIVATE);
                    headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                    return headers;
                }
            };

            RequestHandler.getInstance(itemView.getContext()).addToRequestQueue(req);
        });

    }

    public void Bind(Request_Join_Network rn)
    {
        username.setText(rn.getUsername());
    }
}
