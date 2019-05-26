package com.example.smartcity;

public class Post {
    private String content;
    private String date;
    private String sender;

    public Post(){}

    public Post(String contenu, String horaire, String emetteur)
    {
        content = contenu;
        date = horaire;
        sender = emetteur;
    }

    public String getContent() {return content;}

    public String getDate() {return date;}

    public String getSender() {return sender;}
}
