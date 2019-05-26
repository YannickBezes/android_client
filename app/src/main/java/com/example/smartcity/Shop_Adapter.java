package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Shop_Adapter extends RecyclerView.Adapter<Shop_Holder> {

    List<Shop> shopList;

    public Shop_Adapter(List<Shop> list){
        shopList = list;
    }

    @Override
    public Shop_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_card, viewGroup, false);
        return new Shop_Holder(view);
    }

    @Override
    public void onBindViewHolder(Shop_Holder shop_holder, int position){
        Shop shop = shopList.get(position);
        shop_holder.Bind(shop);
    }

    @Override
    public int getItemCount(){
        return shopList.size();
    }
}
