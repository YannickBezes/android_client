package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostCardHolder> {

    List<Post> postList;
    String network_name;

    public PostAdapter(List<Post> list, String network){
        postList = list;
        network_name = network;
    }

    @Override
    public PostCardHolder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_cards, viewGroup, false);
        return new PostCardHolder(view);
    }

    @Override
    public void onBindViewHolder(PostCardHolder postCardHolder, int position){
        Post post = postList.get(position);
        postCardHolder.Bind(post, network_name);
    }

    @Override
    public int getItemCount(){
        return postList.size();
    }
}
