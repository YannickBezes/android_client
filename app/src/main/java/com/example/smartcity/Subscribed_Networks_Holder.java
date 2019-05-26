package com.example.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Subscribed_Networks_Holder extends RecyclerView.ViewHolder {

    private TextView name;

    public Subscribed_Networks_Holder(View itemView)
    {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.subscribed_network_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://bsy.ovh:5000/network/";
                url += name.getText().toString();
                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        response -> {
                            try {
                                if (response.getBoolean("success")) {
                                    JSONObject json = response.getJSONObject("data");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("network", json.toString());
                                    Intent myIntent = new Intent(itemView.getContext(), DisplayNetwork.class);
                                    myIntent.putExtras(bundle);
                                    itemView.getContext().startActivity(myIntent);
                                } else {
                                    Toast.makeText(itemView.getContext(), "Can't find any network with this name", Toast.LENGTH_SHORT).show();
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
            }
        });

    }

    public void Bind(Subscribed_Network sn)
    {
        name.setText(sn.getName());
    }
}
