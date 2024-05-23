package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует
        if (userId != -1){
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            startActivity(intent);
        }

        Button b1 = findViewById(R.id.autorization);
        Button b2 = findViewById(R.id.registration);
        TextView b3 = findViewById(R.id.no_registration);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Authorization.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainPage.class);
                startActivity(intent);
            }
        });
    }
}