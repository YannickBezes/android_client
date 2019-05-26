package com.example.smartcity;

public class CalendarEvent {

    public String name;
    public String startDate;
    public String endDate;
    public String description;


    public CalendarEvent(String name, String startDate, String endDate, String description) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        if (description != null)
            this.description = description;
        else
            this.description = "";
    }

    @Override
    public String toString() {
        return "<Event " + name + " start : " + startDate + " end : " + endDate + ", desc : " + description;
    }
}
