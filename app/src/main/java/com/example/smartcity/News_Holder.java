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

public class News_Holder extends RecyclerView.ViewHolder {

    private TextView title, subtitle, body, url;

    public News_Holder(View itemView)
    {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.news_card_title);
        subtitle = (TextView) itemView.findViewById(R.id.news_card_subtitle);
        body = (TextView) itemView.findViewById(R.id.news_card_body);
        url = (TextView) itemView.findViewById(R.id.news_card_url);
    }

    public void Bind(News n)
    {
        title.setText(n.getTitle());
        String date = n.getDate();
        date = date.replace("-", " à ");
        subtitle.setText(n.getSource() + " le " + date);
        body.setText(n.getContent());
        url.setText(n.getUrl());
    }
}
