package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        DBHelper dbHelper = new DBHelper(this);

        db = dbHelper.getWritableDatabase();

        EditText login_t = findViewById(R.id.login);
        EditText parol_t = findViewById(R.id.parol);
        EditText parol_t2 = findViewById(R.id.parol2);
        EditText username = findViewById(R.id.username);
        Button reg = findViewById(R.id.regi);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = login_t.getText().toString().trim();
                String parol = parol_t.getText().toString().trim();
                String parol2 = parol_t2.getText().toString().trim();
                String usernameText = username.getText().toString().trim();

                if (login.isEmpty() || parol.isEmpty() || parol2.isEmpty() || usernameText.isEmpty()) {
                    Toast.makeText(Registration.this, "Все поля должны быть заполнены!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (parol.equals(parol2)){
                    dbHelper.addUser(login, usernameText, parol);
                    Toast.makeText(Registration.this, "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registration.this, Authorization.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Registration.this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
