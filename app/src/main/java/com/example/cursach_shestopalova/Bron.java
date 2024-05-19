package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        TextView selectedSeatsTextView = findViewById(R.id.rr);


        StringBuilder selectedSeatsStringBuilder = new StringBuilder();
        for (Integer seatId : selectedSeatsIds) {
            selectedSeatsStringBuilder.append(seatId).append("\n");
        }
        String selectedSeatsString = selectedSeatsStringBuilder.toString();


        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует
        selectedSeatsTextView.setText(String.valueOf(userId));



    }
}