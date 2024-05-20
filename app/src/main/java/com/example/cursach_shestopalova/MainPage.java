package com.example.cursach_shestopalova;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter1 movieAdapter1;
    private MovieAdapter2 movieAdapter2;

    private DBHelper dbHelper;
    private SwitchCompat mySwitch;
    List<Movie> movieList;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF000000")));

        //установка цвета кнопки

        actionBar.setTitle("Ваш текст слева");
// Находим Bottom Navigation Bar по его ID
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);

// Получаем Drawable для установки цвета иконок
        ColorStateList colorStateList = getResources().getColorStateList(R.color.selector_colors_bar);

// Устанавливаем цвет иконок для Bottom Navigation Bar
        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(colorStateList);
        actionBar.setTitle("Фильмы");

        FragmentMovies fragmentMovies = new FragmentMovies();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentMovies)
                .commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.films) {
                    Toast.makeText(MainPage.this, "Фильмы", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Фильмы");

                    // Загружаем фрагмент FragmentMovies
                    FragmentMovies fragmentMovies = new FragmentMovies();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragmentMovies)
                            .commit();

                    return true;
                }

                else if (item.getItemId()==R.id.cinemas){
                    Toast.makeText(MainPage.this, "Кинотеатры", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Кинотеатры");

                    FragmentCinemas fragmentCinemas = new FragmentCinemas();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragmentCinemas)
                            .commit();

                    return true;
                }
                else if (item.getItemId()==R.id.tickets){
                    Toast.makeText(MainPage.this, "Билеты", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Билеты");

                    FragmentMovies fragmentMovies = new FragmentMovies();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragmentMovies)
                            .commit();

                    return true;
                }
                else if (item.getItemId()==R.id.profile){
                    Toast.makeText(MainPage.this, "Профиль", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Профиль");

                    FragmentMovies fragmentMovies = new FragmentMovies();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragmentMovies)
                            .commit();

                    return true;
                }
                return false;
            }
        });
    }





}