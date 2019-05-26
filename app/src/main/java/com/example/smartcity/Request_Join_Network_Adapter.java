package com.example.smartcity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Request_Join_Network_Adapter extends RecyclerView.Adapter<Request_Join_Network_Holder> {

    List<Request_Join_Network> _req;
    String network_name;

    public Request_Join_Network_Adapter(List<Request_Join_Network> list, String network){
        _req = list;
        network_name = network;
    }

    @Override
    public Request_Join_Network_Holder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_join_network_card, viewGroup, false);
        return new Request_Join_Network_Holder(view, network_name);
    }

    @Override
    public void onBindViewHolder(Request_Join_Network_Holder request_join_network_holder, int position){
        Request_Join_Network rn = _req.get(position);
        request_join_network_holder.Bind(rn);
    }

    @Override
    public int getItemCount(){
        return _req.size();
    }
}
