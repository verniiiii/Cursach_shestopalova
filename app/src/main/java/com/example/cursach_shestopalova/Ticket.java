package com.example.cursach_shestopalova;

import java.io.Serializable;

public class Ticket implements Serializable {
    private int id;
    private int screening_id;
    private int place_id;
    private int user_id;

    public Ticket(int id, int screening_id, int place_id, int user_id) {
        this.id = id;
        this.screening_id = screening_id;
        this.place_id = place_id;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public int getScreening_id() {
        return screening_id;
    }

    public int getUser_id() {
        return user_id;
    }
}
