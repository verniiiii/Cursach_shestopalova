package com.example.cursach_shestopalova;

public class Place {
    private int id;
    private int rowNumber;
    private int number;
    private int isOccupied;

    public Place(int id, int rowNumber, int number) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(int isOccupied) {
        this.isOccupied = isOccupied;
    }
}
