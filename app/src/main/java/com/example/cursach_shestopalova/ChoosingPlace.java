package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ChoosingPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_place);

        Screening screening = (Screening) getIntent().getSerializableExtra("selectedScreening");

        TextView textView1 = findViewById(R.id.titleTextView);
        TextView textView2 = findViewById(R.id.cityTextView);
        TextView textView3 = findViewById(R.id.genreTextView);
        textView1.setText(String.valueOf(screening.getCinema_id()));
        textView2.setText(String.valueOf(screening.getMovie_id()));
        textView3.setText(String.valueOf(screening.getDate()));


    }
}