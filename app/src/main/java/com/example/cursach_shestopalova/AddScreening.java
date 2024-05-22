package com.example.cursach_shestopalova;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddScreening extends AppCompatActivity {

    private TextView movieNameTextView;
    private TextView dateTextView;
    private Spinner cinemaSpinner;
    private Spinner hallSpinner;
    private Spinner timeSpinner;
    private EditText priceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screening);

        // Определяем все поля и спиннеры в разметке
        movieNameTextView = findViewById(R.id.movie_name);
        dateTextView = findViewById(R.id.date);
        cinemaSpinner = findViewById(R.id.cinema_spinner);
        hallSpinner = findViewById(R.id.hall_spinner);
        timeSpinner = findViewById(R.id.time_spinner);
        priceEditText = findViewById(R.id.price);

        // Получаем фильм и дату из интента
        int selectedMovieId = getIntent().getIntExtra("movieId", 0);
        String date = getIntent().getStringExtra("date");
        DBHelper dbHelper = new DBHelper(this);
        List<Cinema> cinemasList = dbHelper.getAllCinemas();
        List<String> cinemasNamesList = new ArrayList<>();
        for (Cinema cinema : cinemasList) {
            cinemasNamesList.add(cinema.getName());
        }

        // Вставляем фильм и дату в нужные поля
        movieNameTextView.setText(dbHelper.findNameMovieById(selectedMovieId));
        dateTextView.setText(date);

//        // Заполняем спиннеры списками программно
        ArrayAdapter<String> cinemaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cinemasNamesList);
        cinemaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cinemaSpinner.setAdapter(cinemaAdapter);
        cinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный кинотеатр
                String selectedCinema = parent.getItemAtPosition(position).toString();
                DBHelper dbHelper =new DBHelper(AddScreening.this);
                // Обновляем список номеров залов
                List<String> hallsNamesList = dbHelper.getAllNumberHallsByCinema(position+1);

                // Обновляем спиннер с номерами залов
                ArrayAdapter<String> hallAdapter = new ArrayAdapter<>(AddScreening.this, android.R.layout.simple_spinner_item, hallsNamesList);
                hallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hallSpinner.setAdapter(hallAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Можно оставить пустым, если не нужно обрабатывать событие "ничего не выбрано"
            }
        });


//
//        ArrayAdapter<CharSequence> hallAdapter = ArrayAdapter.createFromResource(this, R.array.halls, android.R.layout.simple_spinner_item);
//        hallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        hallSpinner.setAdapter(hallAdapter);
//
//        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
//        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        timeSpinner.setAdapter(timeAdapter);
    }
}
