package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Calendar_Adapter extends RecyclerView.Adapter<Calendar_Holder> {

    List<CalendarEvent> eventList;

    public Calendar_Adapter(List<CalendarEvent> list){
        eventList = list;
    }

    @Override
    public Calendar_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar_card, viewGroup, false);
        return new Calendar_Holder(view);
    }

    @Override
    public void onBindViewHolder(Calendar_Holder calendar_holder, int position){
        CalendarEvent ce = eventList.get(position);
        calendar_holder.Bind(ce);
    }

    @Override
    public int getItemCount(){
        return eventList.size();
    }
}
