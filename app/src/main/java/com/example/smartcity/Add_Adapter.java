package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Add_Adapter extends RecyclerView.Adapter<Add_Holder> {

    List<Add> addList;

    public Add_Adapter(List<Add> list){
        addList = list;
    }

    @Override
    public Add_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_card, viewGroup, false);
        return new Add_Holder(view);
    }

    @Override
    public void onBindViewHolder(Add_Holder add_holder, int position){
        Add a = addList.get(position);
        add_holder.Bind(a);
    }

    @Override
    public int getItemCount(){
        return addList.size();
    }
}
