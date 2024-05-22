package com.example.cursach_shestopalova;

import android.database.sqlite.SQLiteDatabase;

public class TableCreate {
    public static final String CREATE_USER_TABLE = "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "login TEXT NOT NULL," +
            "username TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "role TEXT DEFAULT 'user')";

    public static final String CREATE_CINEMA_TABLE = "CREATE TABLE cinemas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "city TEXT NOT NULL," +
            "location TEXT NOT NULL," +
            "description TEXT)";

    public static final String CREATE_HALL_TABLE = "CREATE TABLE halls (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cinema_id INTEGER NOT NULL," +
            "hall_number INTEGER NOT NULL," +
            "capacity INTEGER NOT NULL," +
            "role TEXT DEFAULT 'standard'," +
            "FOREIGN KEY (cinema_id) REFERENCES cinemas(id))";

    public static final String CREATE_ROW_TABLE = "CREATE TABLE rows (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "hall_id INTEGER NOT NULL," +
            "capacity INTEGER NOT NULL," +
            "row_number INTEGER NOT NULL," +
            "FOREIGN KEY (hall_id) REFERENCES halls(id))";

    public static final String CREATE_PLACE_TABLE = "CREATE TABLE places (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "row_id INTEGER NOT NULL," +
            "place_number INTEGER NOT NULL," +
            "FOREIGN KEY (row_id) REFERENCES rows(id))";

    public static final String CREATE_MOVIE_TABLE = "CREATE TABLE movies (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "genr TEXT NOT NULL," +
            "director TEXT NOT NULL," +
            "actors TEXT NOT NULL," +
            "city TEXT NOT NULL," +
            "duration INTEGER NOT NULL,"+
            "image_id Text NOT NULL)";

    public static final String CREATE_SCREENING_TABLE = "CREATE TABLE screenings (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cinema_id INTEGER NOT NULL," +
            "movie_id INTEGER NOT NULL," +
            "hall_id INTEGER NOT NULL," +
            "date TEXT NOT NULL," +
            "time TEXT NOT NULL," +
            "price INTEGER NOT NULL," +
            "FOREIGN KEY (cinema_id) REFERENCES cinemas(id),"+
            "FOREIGN KEY (hall_id) REFERENCES halls(id),"+
            "FOREIGN KEY (movie_id) REFERENCES movies(id))";


    public static final String CREATE_TICKET_TABLE = "CREATE TABLE tickets (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "screening_id INTEGER NOT NULL," +
            "place_id INTEGER NOT NULL," +
            "user_id INTEGER NOT NULL," +
            "FOREIGN KEY (screening_id) REFERENCES screenings(id),"+
            "FOREIGN KEY (user_id) REFERENCES users(id),"+
            "FOREIGN KEY (place_id) REFERENCES places(id))";
    public static final String CREATE_QUESTION_TABLE = "CREATE TABLE faqs (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "question TEXT NOT NULL," +
            "answer TEXT NOT NULL)";
    public static String getCreateScreeningTable() {
        return CREATE_SCREENING_TABLE;
    }
    public static String getCREATE_TICKET_TABLE() {
        return CREATE_TICKET_TABLE;
    }
    public static void createTables(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CINEMA_TABLE);
        db.execSQL(CREATE_HALL_TABLE);
        db.execSQL(CREATE_ROW_TABLE);
        db.execSQL(CREATE_PLACE_TABLE);
        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_SCREENING_TABLE);
        db.execSQL(CREATE_TICKET_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
    }
}
