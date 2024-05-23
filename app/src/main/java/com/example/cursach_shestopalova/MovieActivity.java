package com.example.cursach_shestopalova;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MovieActivity extends AppCompatActivity{
    private int movieId;
    private Movie movie;
    private Button selectedButton; // выбранная кнопка
    private RecyclerView recyclerView;
    private ScreeningAdapter screeningAdapter;
    private CinemaAdapter сinemaAdapter;
    private  List<Cinema> allCinemas;
    private List<Cinema> cinemas;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", ""); // возвращаем -1, если ключ отсутствует
        TextView delete_movie = findViewById(R.id.delete_movie);
        TextView textAdmin = findViewById(R.id.add_screening);
        if (userRole.equals("admin")){
            delete_movie.setVisibility(View.VISIBLE);
            textAdmin.setVisibility(View.VISIBLE);
        }
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Получаем id фильма из интента
        Intent intent = getIntent();
        movieId = intent.getIntExtra("movie_id", -1);

        DBHelper dbHelper = new DBHelper(this);
        movie = dbHelper.findMovieById(movieId);

        dbHelper.close();

        ImageView image = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.titleTextView);
        TextView genre = findViewById(R.id.genreTextView);
        TextView city = findViewById(R.id.cityTextView);
        TextView description = findViewById(R.id.descriptionTextView);
        image.setImageResource(movie.getImage_id());
        title.setText(movie.getTitle());
        genre.setText(movie.getGenr());
        city.setText(movie.getCity());
        description.setText(movie.getDescription());
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
        recyclerView = findViewById(R.id.cinemas_recycler_view);

        List<Screening> screenings = new ArrayList<>();
        screeningAdapter = new ScreeningAdapter(screenings, this);

        cinemas = new ArrayList<>();
        сinemaAdapter = new CinemaAdapter(cinemas, this, movieId);
        recyclerView.setAdapter(сinemaAdapter);

// Устанавливаем LayoutManager для RecyclerView после установки адаптера
        recyclerView.setLayoutManager(new LinearLayoutManager(MovieActivity.this, LinearLayoutManager.VERTICAL, false));



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

        delete_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieActivity.this);
                builder.setTitle("Подтверждение удаления");
                builder.setMessage("Действительно ли вы хотите удалить фильм \"" + movie.getTitle() + "\" из всех кинотеатров?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(MovieActivity.this);
                        dbHelper.deleteScreeningsByMovieIdAndTickets(movieId);
                        dbHelper.deleteMovie(movieId);
                        dbHelper.close();
                        Intent intent = new Intent(MovieActivity.this, MainPage.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Отмена", null);
                builder.show();
            }
        });

        textAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieActivity.this, AddScreening.class);
                intent.putExtra("date", date);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });


    }



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
        date = (String) button.getTag();

        // получаем список кинотеатров с сеансами на выбранную дату
        List<Cinema> cinemasWithScreenings = getCinemasWithScreeningsByMovieIdAndDate(movieId, date);

        // обновляем список кинотеатров на основе полученных данных
        сinemaAdapter.setCinemas(cinemasWithScreenings);
    }
    private List<Cinema> getCinemasWithScreeningsByMovieIdAndDate(int movieId, String date) {
        DBHelper dbHelper = new DBHelper(this);
        cinemas = new ArrayList<>();

        // получаем список кинотеатров, которые показывают фильм с id movieId
        allCinemas = dbHelper.getCinemasByMovieId(movieId);

        for (Cinema cinema : allCinemas) {
            List<Screening> screenings = dbHelper.getScreeningsByCinemaIdAndMovieIdAndDate(cinema.getId(), movieId, date);
            if (!screenings.isEmpty()) {
                cinema.setScreenings(screenings);
                cinemas.add(cinema);
            }
        }

        return cinemas;
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