package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class News_Adapter extends RecyclerView.Adapter<News_Holder> {

    List<News> _news;

    public News_Adapter(List<News> list){
        _news = list;
    }

    @Override
    public News_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_card, viewGroup, false);
        return new News_Holder(view);
    }

    @Override
    public void onBindViewHolder(News_Holder news_holder, int position){
        News n = _news.get(position);
        news_holder.Bind(n);
    }

    @Override
    public int getItemCount(){
        return _news.size();
    }
}
