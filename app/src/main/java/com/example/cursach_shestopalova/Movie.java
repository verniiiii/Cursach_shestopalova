package com.example.cursach_shestopalova;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String description;
    private String genr;
    private String city;
    private String director;
    private String actors;
    private int duration;
    private int image_id;
    private List<Screening> screenings;

    public Movie(int id, String title, String description, String genr, String city, String director, String actors, int duration, int image_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genr = genr;
        this.city = city;
        this.director = director;
        this.actors = actors;
        this.duration = duration;
        this.image_id = image_id;
        this.screenings = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenr() {
        return genr;
    }

    public String getCity() {
        return city;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public int getDuration() {
        return duration;
    }

    public int getImage_id() {
        return image_id;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }
}

