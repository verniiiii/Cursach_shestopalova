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
    ImageView imageView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF000000")));

        //установка цвета кнопки
        ImageView imageView = findViewById(R.id.searchButton);
        Drawable drawable = imageView.getDrawable();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN); //PorterDuff.Mode.SRC_IN - это режим смешивания, который определяет, как цвет фильтра будет взаимодействовать с исходным изображением

        actionBar.setTitle("Ваш текст слева");
// Находим Bottom Navigation Bar по его ID
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);

// Получаем Drawable для установки цвета иконок
        ColorStateList colorStateList = getResources().getColorStateList(R.color.selector_colors_bar);

// Устанавливаем цвет иконок для Bottom Navigation Bar
        bottomNavigationView.setItemIconTintList(colorStateList);
        bottomNavigationView.setItemTextColor(colorStateList);
        actionBar.setTitle("Фильмы");




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.films){
                    Toast.makeText(MainPage.this, "Фильмы", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Фильмы");
                    return true;
                }
                else if (item.getItemId()==R.id.cinemas){
                    Toast.makeText(MainPage.this, "Кинотеатры", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Кинотеатры");
                    return true;
                }
                else if (item.getItemId()==R.id.tickets){
                    Toast.makeText(MainPage.this, "Билеты", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Билеты");
                    return true;
                }
                else if (item.getItemId()==R.id.profile){
                    Toast.makeText(MainPage.this, "Профиль", Toast.LENGTH_SHORT).show();
                    actionBar.setTitle("Профиль");
                    return true;
                }
                return false;
            }
        });

        dbHelper = new DBHelper(this);
        movieList = dbHelper.getAllMovies();

        recyclerView = findViewById(R.id.container_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        movieAdapter1 = new MovieAdapter1(movieList, MainPage.this);
        recyclerView.setAdapter(movieAdapter1);

        EditText editText = findViewById(R.id.find);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String find_film = editText.getText().toString();
                List<Movie> filterMovies;
                filterMovies = filterMoviesBySubstring(movieList, find_film);


                if (mySwitch != null && mySwitch.isChecked()) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainPage.this, 1));
                    movieAdapter2 = new MovieAdapter2(filterMovies, MainPage.this);
                    recyclerView.setAdapter(movieAdapter2);
                } else if (mySwitch != null){
                    recyclerView.setLayoutManager(new GridLayoutManager(MainPage.this, 2));
                    movieAdapter1 = new MovieAdapter1(filterMovies, MainPage.this);
                    recyclerView.setAdapter(movieAdapter1);
                }
            }

        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_1, menu);

        MenuItem menuItem = menu.findItem(R.id.action_switch);
        mySwitch = (SwitchCompat) menuItem.getActionView();

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainPage.this, 1));
                    movieAdapter2 = new MovieAdapter2(movieList, MainPage.this);
                    recyclerView.setAdapter(movieAdapter2);
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainPage.this, 2));
                    movieAdapter1 = new MovieAdapter1(movieList, MainPage.this);
                    recyclerView.setAdapter(movieAdapter1);
                }
            }
        });

        return true;
    }
    public List<Movie> filterMoviesBySubstring(List<Movie> movies, String substring) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(substring.toLowerCase())) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }




}