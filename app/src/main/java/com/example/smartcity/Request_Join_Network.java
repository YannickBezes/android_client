package com.example.smartcity;

public class Request_Join_Network {
    private String username;

    public Request_Join_Network(){}

    public Request_Join_Network(String nom)
    {
        username = nom;
    }

    public String getUsername() {return username;}
}
