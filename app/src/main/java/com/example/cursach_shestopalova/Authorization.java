package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Authorization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
        DBHelper db = new DBHelper(this); // Создание экземпляра DBHelper

        EditText login_t = findViewById(R.id.login);
        EditText parol_t = findViewById(R.id.parol);
        Button vhod = findViewById(R.id.vhod);
        vhod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = login_t.getText().toString().trim();
                String parol = parol_t.getText().toString().trim();

                if (login.isEmpty() || parol.isEmpty()) {
                    Toast.makeText(Authorization.this, "Все поля должны быть заполнены!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase dbb = db.getWritableDatabase();
                if (db.checkUserAndPassword(login, parol, dbb)){
                    String t = db.getUserRole(login);
                    if ("user".equals(t)){
                        Intent intent = new Intent(Authorization.this, MainPage.class);
                        startActivity(intent);

                    } else if ("admin".equals(t)) {
                        Toast.makeText(Authorization.this, "Вы зашли как админ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Authorization.this, "Введён неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}