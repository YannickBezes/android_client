package com.example.smartcity;

public class News {
    private String title;
    private String content;
    private String url;
    private String date;
    private String source;

    public News(){}

    public News(String titre, String contenu, String lien, String d, String s)
    {
        title = titre;
        content = contenu;
        url = lien;
        date = d;
        source = s;
    }

    public String getTitle() {return title;}

    public String getContent() {return content;}

    public String getUrl() {return url;}

    public String getDate() {return date;}

    public String getSource() {return source;}
}
