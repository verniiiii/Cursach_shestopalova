package com.example.cursach_shestopalova;

public class Row {
    private int id;
    private int hallId;
    private int capacity;

    public Row(int id, int hallId, int capacity) {
        this.id = id;
        this.hallId = hallId;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

