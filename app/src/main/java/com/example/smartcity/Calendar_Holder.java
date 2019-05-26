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

public class Calendar_Holder extends RecyclerView.ViewHolder {

    private TextView title, description;
    View view;

    public Calendar_Holder(View itemView)
    {
        super(itemView);
        view = itemView;

        title = itemView.findViewById(R.id.event_card_title);
        description = itemView.findViewById(R.id.event_card_description);
    }

    public void Bind(CalendarEvent calendarEvent)
    {
        title.setText(calendarEvent.name + ", " + view.getContext().getResources().getString(R.string.from) + " : " + calendarEvent.startDate + " " + view.getContext().getResources().getString(R.string.to) + " " + calendarEvent.endDate);
        if (calendarEvent.description.equals(""))
            description.setText(view.getContext().getResources().getString(R.string.no_description));
        else
            description.setText(calendarEvent.description);
    }
}
