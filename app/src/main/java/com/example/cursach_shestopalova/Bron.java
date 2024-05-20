package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bron extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bron);
        ArrayList<Integer> selectedSeatsIds = getIntent().getIntegerArrayListExtra("selected_seats");
        Screening screening = (Screening) getIntent().getSerializableExtra("screening");
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует

        TableLayout tableLayout2 = findViewById(R.id.t2);
        TableLayout tableLayout3 = findViewById(R.id.t3);
        tableLayout2.setVisibility(View.GONE);
        tableLayout3.setVisibility(View.GONE);

        TextView film = findViewById(R.id.film);
        TextView cinema = findViewById(R.id.cinema);
        TextView data = findViewById(R.id.data);
        TextView money = findViewById(R.id.money);

        int movie_id = screening.getMovie_id();
        int cinema_id = screening.getCinema_id();
        int hall_id = screening.getHall_id();
        int price = screening.getPrice();
        String date = screening.getDate();
        String time = screening.getTime();
        DBHelper dbHelper = new DBHelper(this);

        film.setText(dbHelper.findNameMovieById(movie_id));
        cinema.setText(dbHelper.findNameCinemaById(cinema_id));
        data.setText(date + ", " + time);
        money.setText(String.valueOf(price*selectedSeatsIds.size()));

        for (int i = 0; i < selectedSeatsIds.size(); i++) {
            int id = selectedSeatsIds.get(i);
            Pair<Integer, Integer> seatAndRow = dbHelper.findSeatAndRowIdById(id);
            int seat_number = seatAndRow.first;
            int row_id = seatAndRow.second;
            TextView place = (TextView) findViewById(getResources().getIdentifier("place" + (i + 1), "id", getPackageName()));
            place.setText(String.valueOf(seat_number));

            int row_number = dbHelper.findRowById(row_id);
            TextView row = (TextView) findViewById(getResources().getIdentifier("row" + (i + 1), "id", getPackageName()));
            row.setText(String.valueOf(row_number));

            int hall_number = dbHelper.findHallById(hall_id);
            TextView hall = (TextView) findViewById(getResources().getIdentifier("hall" + (i + 1), "id", getPackageName()));
            hall.setText(String.valueOf(hall_number));

            TextView mon = (TextView) findViewById(getResources().getIdentifier("money" + (i + 1), "id", getPackageName()));
            mon.setText(String.valueOf(price));

            TableLayout t = (TableLayout) findViewById(getResources().getIdentifier("t" + (i + 1), "id", getPackageName()));
            t.setVisibility(View.VISIBLE);
        }
        Button broni = findViewById(R.id.broni);
        broni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedSeatsIds.size(); i++) {
                    dbHelper.addTickets(screening.getId(), selectedSeatsIds.get(i), userId);
                }
                Toast.makeText(Bron.this, "Билеты забронированы", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Bron.this, MainPage.class);
                startActivity(intent);


            }
        });



    }

}