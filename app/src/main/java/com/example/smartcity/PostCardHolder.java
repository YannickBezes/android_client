package com.example.smartcity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.smartcity.Post;
import com.example.smartcity.R;

public class PostCardHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView content;
    private View view;

    public PostCardHolder(View itemView)
    {
        super(itemView);
        view = itemView;
        title = (TextView) itemView.findViewById(R.id.post_card_title);
        content = (TextView) itemView.findViewById(R.id.post_card_content);
    }

    public void Bind(Post post, String network_name)
    {
        if (post.getSender().equals("") && post.getDate().equals(""))
        {
            content.setText(post.getContent());
            content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            content.setTextColor(Color.BLACK);
            content.setOnClickListener(v -> {
                Bundle bundle_network_name = new Bundle();
                bundle_network_name.putString("network_name", network_name);
                Intent myIntent = new Intent(view.getContext(), RequestsToNetwork.class);
                myIntent.putExtras(bundle_network_name);
                view.getContext().startActivity(myIntent);
            });
        }
        else {
            title.setText("Posté par " + post.getSender() + ", le " + post.getDate());
            content.setText(post.getContent());
        }
    }
}
