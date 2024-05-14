package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ChoosingPlace extends AppCompatActivity {

    private Screening screening;
    private TextView toolbarTitle;
    private TextView cinemaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_place);

        screening = (Screening) getIntent().getSerializableExtra("selectedScreening");

        toolbarTitle = findViewById(R.id.toolbar_title);
        cinemaName = findViewById(R.id.cinema_name);

        // Получаем идентификатор фильма из объекта Screening
        int movieId = screening.getMovie_id();

        // Выполняем запрос к базе данных, чтобы получить название фильма
        DBHelper databaseHelper = new DBHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("movies", new String[]{"title"}, "id = ?", new String[]{String.valueOf(movieId)}, null, null, null);

        // Извлекаем название фильма из курсора
        if (cursor.moveToFirst()) {
            String movieTitle = cursor.getString(0);
            toolbarTitle.setText(movieTitle);
        }

        // Получаем идентификатор кинотеатра из объекта Screening
        int cinemaId = screening.getCinema_id();

        // Выполняем запрос к базе данных, чтобы получить название кинотеатра
        cursor = db.query("cinemas", new String[]{"name"}, "id = ?", new String[]{String.valueOf(cinemaId)}, null, null, null);

        // Извлекаем название кинотеатра из курсора
        if (cursor.moveToFirst()) {
            String cinemaNameText = cursor.getString(0);
            cinemaName.setText(cinemaNameText);
        }



        // Получаем дату, номер зала и время из объекта Screening
        String date = screening.getDate();
        int hallNumber = screening.getHall_id();
        String time = screening.getTime();

// Выполняем запрос к базе данных, чтобы получить номер зала, вместимость и роль
        cursor = db.query("halls", new String[]{"hall_number", "capacity", "role"}, "id = ?", new String[]{String.valueOf(hallNumber)}, null, null, null);

// Извлекаем номер зала, вместимость и роль из курсора
        int hallNum = 0;
        int capacity = 0;
        String role = "";
        if (cursor.moveToFirst()) {
            hallNum = cursor.getInt(0);
            capacity = cursor.getInt(1);
            role = cursor.getString(2);
        }

// Собираем строку с описанием сеанса
        String sessionDescription = String.format("%s, зал %d (%s), %d мест, %s", date, hallNum, role, capacity, time);

// Выводим строку с описанием сеанса в соответствующее поле
        TextView sessionDescriptionView = findViewById(R.id.session_description);
        sessionDescriptionView.setText(sessionDescription);

// Закрываем курсор и базу данных
        cursor.close();
        db.close();


        RecyclerView sessionsRecyclerView = findViewById(R.id.sessions_recycler_view);
        ArrayList<Screening> screenings = (ArrayList<Screening>) getIntent().getSerializableExtra("screenings");
        ScreeningAdapter_bron adapter = new ScreeningAdapter_bron(screenings, this);
        sessionsRecyclerView.setAdapter(adapter);
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(ChoosingPlace.this, LinearLayoutManager.HORIZONTAL, false));


    }
}

