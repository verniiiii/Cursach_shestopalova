package com.example.cursach_shestopalova;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF000000")));
            actionBar.setTitle("Фильмы");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);

        // Устанавливаем цвет иконок для Bottom Navigation Bar
        ColorStateList colorStateList = getResources().getColorStateList(R.color.selector_colors_bar);
        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(colorStateList);

        loadFragment(new FragmentMovies());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                String title = "";

                if (item.getItemId() == R.id.films) {
                    selectedFragment = new FragmentMovies();
                    title = "Фильмы";
                } else if (item.getItemId() == R.id.cinemas) {
                    selectedFragment = new FragmentCinemas();
                    title = "Кинотеатры";
                } else if (item.getItemId() == R.id.tickets) {
                    selectedFragment = new FragmentTickets(); // Замените на FragmentTickets
                    title = "Билеты";
                } else if (item.getItemId() == R.id.profile) {
                    title = "Профиль";
                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует
                    if (userId==-1){
                        selectedFragment = new FragmentProfile(); // Замените на FragmentProfile
                    }
                    else {
                        selectedFragment = new FragmentProfile(); // Замените на FragmentProfile
                    }
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    if (actionBar != null) {
                        actionBar.setTitle(title);
                    }
                }

                Toast.makeText(MainPage.this, title, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
