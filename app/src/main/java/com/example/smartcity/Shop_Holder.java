package com.example.smartcity;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Shop_Holder extends RecyclerView.ViewHolder {

    private TextView name, address, category;
    private ImageButton fav;
    View view;

    public Shop_Holder(View itemView)
    {
        super(itemView);
        view = itemView;

        name = itemView.findViewById(R.id.shop_card_name);
        address = itemView.findViewById(R.id.shop_card_address);
        category = itemView.findViewById(R.id.shop_card_category);
        fav = itemView.findViewById(R.id.add_shop_to_fav);
    }

    public void Bind(Shop shop)
    {
        name.setText(shop.name);
        address.setText(shop.address);
        category.setText(view.getResources().getString(R.string.category) + shop.category);
        if (shop.fav)
            fav.setImageResource(R.drawable.fav_yellow);
        fav.setOnClickListener(v ->{
            String url = "http://bsy.ovh:5000/user/shop/" + shop.name;
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    null,
                    response -> {
                        try {
                            if (response.getBoolean("success")) {
                                if (!shop.fav){
                                    fav.setImageResource(R.drawable.fav_yellow);
                                    Toast.makeText(view.getContext(), R.string.add_to_fav, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    fav.setImageResource(R.drawable.fav);
                                    Toast.makeText(view.getContext(), R.string.remove_from_fav, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(view.getContext(), "Error while adding to favorite", Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("user_prefs", view.getContext().MODE_PRIVATE);
                    headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                    return headers;
                }
            };

            RequestHandler.getInstance(view.getContext()).addToRequestQueue(req);
        });
    }
}
