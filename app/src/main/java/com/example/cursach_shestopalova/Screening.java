package com.example.cursach_shestopalova;

import java.io.Serializable;

public class Screening implements Serializable {
    private int id;
    private int cinema_id;
    private int movie_id;
    private int hall_id;
    private int price;
    private String date;
    private String time;
    public Screening(int id, int cinema_id, int movie_id, int hall_id, int price, String date, String time){
        this.id=id;
        this.cinema_id=cinema_id;
        this.movie_id=movie_id;
        this.hall_id=hall_id;
        this.price=price;
        this.date=date;
        this.time=time;
    }

    public int getId() {
        return id;
    }

    public int getCinema_id() {
        return cinema_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public int getPrice() {
        return price;
    }

    public int getHall_id() {
        return hall_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
