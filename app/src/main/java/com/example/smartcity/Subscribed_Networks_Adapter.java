package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Subscribed_Networks_Adapter extends RecyclerView.Adapter<Subscribed_Networks_Holder> {

    List<Subscribed_Network> snList;

    public Subscribed_Networks_Adapter(List<Subscribed_Network> list){
        snList = list;
    }

    @Override
    public Subscribed_Networks_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subscribed_networks_cards, viewGroup, false);
        return new Subscribed_Networks_Holder(view);
    }

    @Override
    public void onBindViewHolder(Subscribed_Networks_Holder postCardHolder, int position){
        Subscribed_Network sn = snList.get(position);
        postCardHolder.Bind(sn);
    }

    @Override
    public int getItemCount(){
        return snList.size();
    }
}
