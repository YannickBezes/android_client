package com.example.smartcity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActualitiesNewsFragment extends Fragment {

    public ActualitiesNewsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(checkNetworkConnection()) {
            get_news();
        }
        else
            Toast.makeText(getActivity(), "Not Connected!", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.news_fragment, container, false);
    }

    public void get_news()
    {
        List<News> _news = new ArrayList<>();
        String url = "http://bsy.ovh:5000/news";
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Log.d("response_news", response.toString());
                            RecyclerView recyclerView = getActivity().findViewById(R.id.NewsFragmentRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            JSONArray articles = response.getJSONArray("articles");
                            for (int i=0; i < articles.length(); i++)
                            {
                                String title = articles.getJSONObject(i).getString("title");
                                String content = articles.getJSONObject(i).getString("body");
                                String date = articles.getJSONObject(i).getString("dateTime");
                                String link = articles.getJSONObject(i).getString("url");
                                String source = articles.getJSONObject(i).getString("source");

                                News n = new News(title, content, link, date, source);

                                _news.add(n);
                            }

                            recyclerView.setAdapter(new News_Adapter(_news));

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_news), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException err) {Log.d("json", "error");}
                },
                error -> Log.d("Error.Response", error.toString())
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE);
                headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                return headers;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(req);
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // connected

        } else {
            // not connected
            Log.e("internet", "no connection");
        }

        return isConnected;
    }

}
