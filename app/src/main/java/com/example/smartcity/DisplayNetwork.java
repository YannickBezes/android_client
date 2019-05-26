package com.example.smartcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONStringer;
import org.xml.sax.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayNetwork extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_network);

        ImageButton menu_icon = findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener((v) -> {
            startActivity(new Intent(DisplayNetwork.this, MenuActivity.class));
        });

        Bundle bundle = getIntent().getExtras();
        Log.d("bundle", bundle.getString("network"));
        String s_bundle = bundle.getString("network");
        String[] parser = s_bundle.split("\\[|\\]");
        String posts = parser[1];
        String sub_requests = parser[3];
        String subscribers = parser[5];
        String[] parser2 = parser[0].split("\"");
        String network_name = parser2[3];
        String[] parser3 = parser[2].split("[:,]+");
        String state = parser3[2];
        Log.d("nom", network_name);
        Log.d("posts", posts);
        Log.d("public", state);
        Log.d("subscribers", subscribers);
        Log.d("sub_requests", sub_requests);

        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (state.equals("false") && !subscribers.contains(sp.getString("username", null))) {
            Bundle bundle_network_name = new Bundle();
            bundle_network_name.putString("network", network_name);
            Intent myIntent = new Intent(DisplayNetwork.this, PrivateNetwork.class);
            myIntent.putExtras(bundle_network_name);
            startActivity(myIntent);
        } else {
            if (subscribers.contains(sp.getString("username", null))) {
                TextView networktitle = findViewById(R.id.networktitle);
                networktitle.setText(network_name);
            } else {
                TextView networktitle = findViewById(R.id.networktitle);
                networktitle.setText(network_name + " (subscribe)");
                networktitle.setOnClickListener(v ->
                {
                    String url = "http://bsy.ovh:5000/network/";
                    url += network_name + "/" + sp.getString("username", null);
                    JsonObjectRequest req = new JsonObjectRequest(
                            Request.Method.PUT,
                            url,
                            null,
                            response -> {
                                try {
                                    if (response.getBoolean("success")) {
                                        Toast.makeText(DisplayNetwork.this, "Vous vous êtes abonné à ce réseau", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DisplayNetwork.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException err) {
                                    Log.d("json", "error");
                                }
                            },
                            error -> Log.d("Error.Response", error.toString())
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            headers.put("x-access-token", sharedPreferences.getString("user_token", null));
                            return headers;
                        }
                    };

                    RequestHandler.getInstance(this).addToRequestQueue(req);
                });
            }
        }

        List<Post> _posts = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.NetworksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int nb_posts = posts.split("\\}").length;
        Log.d("nb_posts", Integer.toString(nb_posts));
        posts = "," + posts;

        // final String regex_contenu = "\"content\":\"[\\w+\\s?,?\\.?\\\n?]+\",\"date\"";
        final String regex_date = "\"date\":\"\\d+\\/\\d+\\/\\d+-\\d+:\\d+\"";
        final String regex_sender = "\"sender\":\"\\w+\"";

        posts = posts.replace("\\/", "/");

        if (state.equals("false")) {
            Post see_req = new Post("Voir les demandes d'adhésion", "", "");
            _posts.add(see_req);


            if (!posts.equals(",") || _posts.size() != 0) {
                if (!posts.equals(",")) {
                    for (String post : posts.split("\\}")) {
                        String _date = getString(regex_date, post);
                        _date = _date.substring(8, _date.length() - 1);

                        String _sender = getString(regex_sender, post);
                        _sender = _sender.substring(10, _sender.length() - 1);

                        String contenu = post.substring(13, post.indexOf(_date) - 10);
                        contenu = contenu.replace("\\n", System.getProperty("line.separator"));

                        _date = _date.replace("-", " à ");
                        Post p = new Post(contenu, _date, _sender);
                        _posts.add(p);
                    }
                }

                recyclerView.setAdapter(new PostAdapter(_posts, network_name));
            } else
                Toast.makeText(DisplayNetwork.this, "Aucun post sur ce réseau", Toast.LENGTH_LONG).show();
        } else {
            if (!posts.equals(",") || _posts.size() != 0) {
                if (!posts.equals(",")) {
                    for (String post : posts.split("\\}")) {
                        String _date = getString(regex_date, post);
                        _date = _date.substring(8, _date.length() - 1);

                        String _sender = getString(regex_sender, post);
                        _sender = _sender.substring(10, _sender.length() - 1);

                        String contenu = post.substring(13, post.indexOf(_date) - 10);
                        contenu = contenu.replace("\\n", System.getProperty("line.separator"));

                        _date = _date.replace("-", " à ");
                        Post p = new Post(contenu, _date, _sender);
                        _posts.add(p);
                    }
                }

                recyclerView.setAdapter(new PostAdapter(_posts, network_name));
            } else
                Toast.makeText(DisplayNetwork.this, "Aucun post sur ce réseau", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = findViewById(R.id.network_message_fab);

        fab.setOnClickListener(v -> {
            Bundle post_message = new Bundle();
            post_message.putString("name", network_name);
            Intent myIntent = new Intent(DisplayNetwork.this, NetworkPost.class);
            myIntent.putExtras(post_message);
            startActivity(myIntent);
        });
    }

    public String getString(String regex, String content) {
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(content);

        if(matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
