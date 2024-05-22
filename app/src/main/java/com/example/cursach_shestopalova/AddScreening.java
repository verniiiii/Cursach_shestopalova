package com.example.cursach_shestopalova;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddScreening extends AppCompatActivity {

    private TextView movieNameTextView;
    private TextView dateTextView;
    private Spinner cinemaSpinner;
    private Spinner hallSpinner;
    private Spinner screening_spinner;
    private EditText priceEditText;
    private EditText timeEditText;
    private List<Integer> hallsIndicesList;
    private String date;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screening);

        // Определяем все поля и спиннеры в разметке
        movieNameTextView = findViewById(R.id.movie_name);
        dateTextView = findViewById(R.id.date);
        cinemaSpinner = findViewById(R.id.cinema_spinner);
        hallSpinner = findViewById(R.id.hall_spinner);
        priceEditText = findViewById(R.id.price);
        screening_spinner = findViewById(R.id.screening_spinner);
        save = findViewById(R.id.create_screening_button);
        timeEditText = findViewById(R.id.time);

        // Получаем фильм и дату из интента
        int selectedMovieId = getIntent().getIntExtra("movieId", 0);
        date = getIntent().getStringExtra("date");
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
                DBHelper dbHelper = new DBHelper(AddScreening.this);
                // Обновляем список номеров залов
                List<String> hallsNamesList = new ArrayList<>();
                hallsIndicesList = new ArrayList<>();

                List<Pair<Integer, String>> hallsList = dbHelper.getAllNumberHallsByCinema(position + 1);
                for (Pair<Integer, String> hall : hallsList) {
                    hallsNamesList.add(hall.second);
                    hallsIndicesList.add(hall.first);
                }
                // Обновляем спиннер с номерами залов
                ArrayAdapter<String> hallAdapter = new ArrayAdapter<>(AddScreening.this, android.R.layout.simple_spinner_item, hallsNamesList);
                hallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hallSpinner.setAdapter(hallAdapter);

                // Сбрасываем выбранный зал
                hallSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Можно оставить пустым, если не нужно обрабатывать событие "ничего не выбрано"
            }
        });

        hallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный зал
                int selectedHallIndex = hallsIndicesList.get(position);
                DBHelper dbHelper = new DBHelper(AddScreening.this);
                // Обновляем список времени
                List<String> timeList = dbHelper.getSessionStartTimesWithEndTimes(selectedHallIndex, date);
                // Обновляем спиннер с временами
                ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(AddScreening.this, android.R.layout.simple_spinner_item, timeList);
                timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                screening_spinner.setAdapter(timeAdapter);

                // Сбрасываем выбранное время
                screening_spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Можно оставить пустым, если не нужно обрабатывать событие "ничего не выбрано"
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем выбранные значения из спиннеров и полей ввода
                int selectedCinemaIndex = cinemaSpinner.getSelectedItemPosition() + 1;
                int selectedHallIndex = hallsIndicesList.get(hallSpinner.getSelectedItemPosition());
                String priceText = priceEditText.getText().toString();
                String selectedTime = timeEditText.getText().toString();

                // Проверяем, заполнены ли все поля
                if (selectedCinemaIndex > 0 && selectedHallIndex > 0 && !selectedTime.isEmpty() && !priceText.isEmpty()) {
                    // Проверяем формат времени
                    if (selectedTime.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
                        // Проверяем, что цена является корректным числом
                        try {
                            int price = Integer.parseInt(priceText);

                            // Добавляем сеанс в базу данных
                            DBHelper dbHelper = new DBHelper(AddScreening.this);
                            dbHelper.addScreening(selectedCinemaIndex, selectedMovieId, selectedHallIndex, date, selectedTime, price);

                            // Выводим сообщение об успешном добавлении сеанса
                            Toast.makeText(AddScreening.this, "Сеанс успешно добавлен", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddScreening.this, MainPage.class);
                            startActivity(intent);
                        } catch (NumberFormatException e) {
                            // Выводим сообщение об ошибке формата цены
                            Toast.makeText(AddScreening.this, "Неверный формат цены", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Выводим сообщение об ошибке формата времени
                        Toast.makeText(AddScreening.this, "Неверный формат времени", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Выводим сообщение об ошибке
                    Toast.makeText(AddScreening.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
