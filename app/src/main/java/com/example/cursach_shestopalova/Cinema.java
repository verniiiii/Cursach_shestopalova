package com.example.cursach_shestopalova;

import java.io.Serializable;
import java.util.List;

public class Cinema implements Serializable {
    private int id;
    private String name;
    private String city;
    private String location;
    private String description;
    private List<Screening> screenings;

    public Cinema(int id, String name, String city, String location, String description) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }
}
