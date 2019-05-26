package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Holder> {

    List<Category> cat_List;

    public Category_Adapter(List<Category> list){
        cat_List = list;
    }

    @Override
    public Category_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shops_category_card, viewGroup, false);
        return new Category_Holder(view);
    }

    @Override
    public void onBindViewHolder(Category_Holder ch, int position){
        Category cat = cat_List.get(position);
        ch.Bind(cat);
    }

    @Override
    public int getItemCount(){
        return cat_List.size();
    }
}
