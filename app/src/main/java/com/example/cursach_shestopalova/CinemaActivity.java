package com.example.cursach_shestopalova;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cursach_shestopalova.Cinema;
import com.example.cursach_shestopalova.CinemaAdapter;
import com.example.cursach_shestopalova.DBHelper;
import com.example.cursach_shestopalova.Movie;
import com.example.cursach_shestopalova.R;
import com.example.cursach_shestopalova.Screening;
import com.example.cursach_shestopalova.ScreeningAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CinemaActivity extends AppCompatActivity {
    private int cinemaId;
    private Cinema cinema;

    private Button selectedButton; // выбранная кнопка
    private RecyclerView recyclerView;
    private ScreeningAdapter screeningAdapter;
    private MovieAdapterWithScreening movieAdapterWithScreening;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema);
        // Получаем id фильма из интента
        Intent intent = getIntent();
        cinemaId = intent.getIntExtra("cinema_id", -1);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DBHelper dbHelper = new DBHelper(this);
        cinema = dbHelper.findCinemaById(cinemaId);

        dbHelper.close();

        TextView name = findViewById(R.id.cinema_name);
        TextView city = findViewById(R.id.cinema_city);
        TextView location = findViewById(R.id.cinema_location);
        TextView description = findViewById(R.id.cinema_description);
        name.setText(cinema.getName());
        city.setText(cinema.getCity());
        location.setText(cinema.getLocation());
        description.setText(cinema.getDescription());

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    setButtonDateText(button1, calendar);
                    break;
                case 1:
                    setButtonDateText(button2, calendar);
                    break;
                case 2:
                    setButtonDateText(button3, calendar);
                    break;
                case 3:
                    setButtonDateText(button4, calendar);
                    break;
                case 4:
                    setButtonDateText(button5, calendar);
                    break;
                default:
                    break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // Создаем пустой список сеансов для начала
        recyclerView = findViewById(R.id.movies_recycler_view);

        List<Screening> screenings = new ArrayList<>();
        screeningAdapter = new ScreeningAdapter(screenings, this);

        List<Movie> movies = new ArrayList<>();
        movieAdapterWithScreening = new MovieAdapterWithScreening(movies, this);
        recyclerView.setAdapter(movieAdapterWithScreening);
//
// Устанавливаем LayoutManager для RecyclerView после установки адаптера
        recyclerView.setLayoutManager(new LinearLayoutManager(CinemaActivity.this, LinearLayoutManager.VERTICAL, false));

        selectButton(button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button3);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button4);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button5);
            }
        });


    }
//    private int movieId;
//    private Movie movie;
//    private Button selectedButton; // выбранная кнопка
//    private RecyclerView recyclerView;
//    private ScreeningAdapter screeningAdapter;
//    private CinemaAdapter сinemaAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie);
//        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
//

//



//
//

//    }
//
//
//
    private void setButtonDateText(Button button, Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(calendar.getTime());
        button.setTag(dateText); // сохраняем дату в нужном формате в теге кнопки

        dateFormat = new SimpleDateFormat("E dd.MM", Locale.getDefault());
        dateText = dateFormat.format(calendar.getTime());
        button.setText(dateText); // устанавливаем текст кнопки в нужном формате
    }

    private void selectButton(Button button) {
        // сбрасываем цвет фона и надписи для всех кнопок
        resetButtons();

        // меняем цвет фона и надписи для выбранной кнопки
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.active_icon_color));
        button.setTextColor(ContextCompat.getColor(this, R.color.black));

        // сохраняем выбранную кнопку
        selectedButton = button;

        // получаем дату в нужном формате из тега выбранной кнопки
        String date = (String) button.getTag();

        // получаем список кинотеатров с сеансами на выбранную дату
        List<Movie> moviesWithScreenings = getMoviesWithScreeningsByCinemaIdAndDate(cinemaId, date);


        // обновляем список кинотеатров на основе полученных данных
        movieAdapterWithScreening.setMovies(moviesWithScreenings);
    }
    private List<Movie> getMoviesWithScreeningsByCinemaIdAndDate(int cinemaId, String date) {
        DBHelper dbHelper = new DBHelper(this);
        List<Movie> movies = new ArrayList<>();

        // получаем список фильмов, которые показывают в кинотеатре с id cinemaId
        List<Movie> allMovies = dbHelper.getMoviesByCinemaId(cinemaId);



        for (Movie movie : allMovies) {
            List<Screening> screenings = dbHelper.getScreeningsByCinemaIdAndMovieIdAndDate(cinemaId, movie.getId(), date);
            if (!screenings.isEmpty()) {
                movie.setScreenings(screenings);
                movies.add(movie);
            }
        }

        return movies;
    }



    private void resetButtons() {
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);

        button1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        button1.setTextColor(ContextCompat.getColor(this, R.color.inactive_icon_color));

        button2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        button2.setTextColor(ContextCompat.getColor(this, R.color.inactive_icon_color));

        button3.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        button3.setTextColor(ContextCompat.getColor(this, R.color.inactive_icon_color));

        button4.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        button4.setTextColor(ContextCompat.getColor(this, R.color.inactive_icon_color));

        button5.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        button5.setTextColor(ContextCompat.getColor(this, R.color.inactive_icon_color));
    }



}