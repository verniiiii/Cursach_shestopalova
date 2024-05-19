package com.example.cursach_shestopalova;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.gridlayout.widget.GridLayout;
import androidx.gridlayout.widget.GridLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class ChoosingPlace extends AppCompatActivity {

    private Screening screening;
    private TextView toolbarTitle;
    private TextView cinemaName;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private GestureDetector gestureDetector;
    private float x, y;
    private int selectedScreeningPosition = -1;
    private GridLayout seatTable;
    private ArrayList<Integer> selectedSeats = new ArrayList<>();
    private Button continueButton;

    private ArrayList<Place> selectedPlaces;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_place);

        screening = (Screening) getIntent().getSerializableExtra("selectedScreening");

        seatTable = findViewById(R.id.seat_grid);
        toolbarTitle = findViewById(R.id.toolbar_title);
        cinemaName = findViewById(R.id.cinema_name);
        continueButton = findViewById(R.id.continue_button);
        continueButton.setEnabled(false);

        // Получаем идентификатор фильма из объекта Screening
        int movieId = screening.getMovie_id();

        // Выполняем запрос к базе данных, чтобы получить название фильма
        DBHelper databaseHelper = new DBHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("movies", new String[]{"title"}, "id = ?", new String[]{String.valueOf(movieId)}, null, null, null);

        // Извлекаем название фильма из курсора
        if (cursor.moveToFirst()) {
            String movieTitle = cursor.getString(0);
            toolbarTitle.setText(movieTitle);
        }

        // Получаем идентификатор кинотеатра из объекта Screening
        int cinemaId = screening.getCinema_id();

        // Выполняем запрос к базе данных, чтобы получить название кинотеатра
        cursor = db.query("cinemas", new String[]{"name"}, "id = ?", new String[]{String.valueOf(cinemaId)}, null, null, null);

        // Извлекаем название кинотеатра из курсора
        if (cursor.moveToFirst()) {
            String cinemaNameText = cursor.getString(0);
            cinemaName.setText(cinemaNameText);
        }



        // Получаем дату, номер зала и время из объекта Screening
        String date = screening.getDate();
        int hallNumber = screening.getHall_id();
        String time = screening.getTime();

// Выполняем запрос к базе данных, чтобы получить номер зала, вместимость и роль
        cursor = db.query("halls", new String[]{"hall_number", "capacity", "role"}, "id = ?", new String[]{String.valueOf(hallNumber)}, null, null, null);

// Извлекаем номер зала, вместимость и роль из курсора
        int hallNum = 0;
        int capacity = 0;
        String role = "";
        if (cursor.moveToFirst()) {
            hallNum = cursor.getInt(0);
            capacity = cursor.getInt(1);
            role = cursor.getString(2);
        }

// Собираем строку с описанием сеанса
        String sessionDescription = String.format("%s, зал %d (%s), %d мест, %s", date, hallNum, role, capacity, time);

// Выводим строку с описанием сеанса в соответствующее поле
        TextView sessionDescriptionView = findViewById(R.id.session_description);
        sessionDescriptionView.setText(sessionDescription);




        ArrayList<Screening> screenings = (ArrayList<Screening>) getIntent().getSerializableExtra("screenings");

// Находим позицию выбранного сеанса в списке сеансов
        int selectedScreeningPosition = screenings.indexOf(screening);

        ScreeningAdapter_bron adapter = new ScreeningAdapter_bron(screenings, this, screening, this);




        RecyclerView sessionsRecyclerView = findViewById(R.id.sessions_recycler_view);


        sessionsRecyclerView.setAdapter(adapter);
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(ChoosingPlace.this, LinearLayoutManager.HORIZONTAL, false));

        int selectedSeatsCount = 0;

        int totalPrice = 0;

        TextView ticketsCountView = findViewById(R.id.tickets_count);
        ticketsCountView.setText(getString(R.string.tickets_count, selectedSeatsCount));

        TextView totalPriceView = findViewById(R.id.total_price);
        totalPriceView.setText(getString(R.string.total_price, totalPrice));




    }
    public void updateHallView(Screening selectedScreening) {

        DBHelper databaseHelper = new DBHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        seatTable.removeAllViews(); // очищаем GridLayout
        int hallId = selectedScreening.getHall_id();

        // Обновляем отображение зала в зависимости от выбранного сеанса
        ArrayList<Row> rows = new ArrayList<>();

        String query = "SELECT * FROM rows WHERE hall_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(hallId)});
        Log.d("Places", "Cursor count: " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int hall_id = cursor.getInt(1);
                int capacityy = cursor.getInt(2);

                Row row = new Row(id, hall_id, capacityy);
                rows.add(row);
            } while (cursor.moveToNext());
        }

        Log.d("Places", "22: " + rows.size());

        // теперь в списке rows содержатся все ряды, относящиеся к залу с идентификатором hallId

        ArrayList<Place> places = new ArrayList<>();

        query = "SELECT places.id, places.place_number, rows.row_number " +
                "FROM places " +
                "JOIN rows ON places.row_id = rows.id " +
                "JOIN halls ON rows.hall_id = halls.id " +
                "WHERE halls.id = ?";

        Log.d("Places", "Query: " + query);
        Log.d("Places", "Hall ID: " + hallId);

        cursor = db.rawQuery(query, new String[]{String.valueOf(hallId)});

        Log.d("Places", "Cursor count: " + cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int placeNumber = cursor.getInt(1);
                int rowNumber = cursor.getInt(2);

                Place place = new Place(id, rowNumber, placeNumber);
                places.add(place);

                Log.d("Places", "Place ID: " + id + ", Place number: " + placeNumber + ", Row number: " + rowNumber);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Log.d("Places", "Number of places: " + places.size());

        // Получаем максимальное количество мест в ряду и всего в зале
        int maxSeatsInRow = 0;
        int totalSeats = 0;
        for (Place place : places) {
            if (place.getRowNumber() > maxSeatsInRow) {
                maxSeatsInRow = place.getRowNumber();
            }
            totalSeats++;
        }

        // Создаем GridLayout с нужным количеством строк и столбцов
        seatTable.setRowCount(maxSeatsInRow +1); // +1 для номеров рядов
        int numColumns = (int) Math.ceil((double) totalSeats / maxSeatsInRow);
        seatTable.setColumnCount(numColumns + 3);

        // Добавляем TextView с номерами рядов в левый столбец
        for (int i = 1; i <= maxSeatsInRow; i++) {
            TextView rowNumberView = new TextView(this);
            rowNumberView.setText(String.valueOf(i));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(10, 10, 10, 10); // отступы между местами
            params.columnSpec = GridLayout.spec(0, 1);
            params.rowSpec = GridLayout.spec(i, 1);

            seatTable.addView(rowNumberView, params);
        }
        // Добавляем номера рядов в правый столбец
        for (int i = 1; i <= maxSeatsInRow; i++) {
            TextView rowNumberView = new TextView(this);
            rowNumberView.setText(String.valueOf(i));
            rowNumberView.setGravity(Gravity.CENTER); // выравнивание текста по центру

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(10, 10, 10, 10); // отступы между местами
            params.columnSpec = GridLayout.spec(numColumns + 1, 1); // +1 для номеров рядов справа
            params.rowSpec = GridLayout.spec(i, 1);

            seatTable.addView(rowNumberView, params);
        }

// Получаем идентификатор сеанса из объекта Screening
        int screeningId = screening.getId();

        // Выполняем запрос к базе данных, чтобы получить все билеты на этот сеанс
        databaseHelper = new DBHelper(this);
        db = databaseHelper.getReadableDatabase();
        cursor = db.query("tickets", new String[]{"place_id"}, "screening_id = ?", new String[]{String.valueOf(screeningId)}, null, null, null);

        // Создаем список из идентификаторов занятых мест
        ArrayList<Integer> occupiedPlaces = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int placeId = cursor.getInt(0);
                occupiedPlaces.add(placeId);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // Добавляем TextView с номерами мест в остальные столбцы
        int rowIndex = 1;
        int columnIndex = 1;
        // Добавляем обработчик нажатий для каждого места
        for (Place place : places) {
            TextView seatView = new TextView(this);
            seatView.setText(String.valueOf(place.getNumber()));

            // Проверяем, занято ли место, и изменяем цвет в зависимости от этого
            if (occupiedPlaces.contains(place.getId())) {
                seatView.setBackgroundResource(R.drawable.i_profile);
                seatView.setClickable(false); // Делаем занятые места некликабельными
            } else {
                seatView.setBackgroundResource(R.drawable.i_films);
                seatView.setClickable(true); // Делаем свободные места кликабельными

                // Добавляем обработчик нажатий для свободных мест
                seatView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView seatView = (TextView) v;
                        int seatId = place.getId(); // Получаем идентификатор места

                        // Проверяем, сколько мест уже выбрано
                        int selectedSeatsCount = 0;
                        for (Integer selectedSeatId : selectedSeats) {
                            if (selectedSeatId == seatId) {
                                // Если место уже выбрано, удаляем его из списка выбранных мест и меняем цвет
                                selectedSeats.remove(selectedSeatId);
                                seatView.setBackgroundResource(R.drawable.i_films);

                                return;
                            }
                            selectedSeatsCount++;
                        }


                        // Если выбрано менее 3 мест, добавляем место в список выбранных мест и меняем цвет
                        if (selectedSeatsCount < 3) {
                            selectedSeats.add(seatId);
                            seatView.setBackgroundResource(R.drawable.i_lupa); // Добавьте свой ресурс для выбранного места
                        } else {
                            // Если выбрано 3 места, показываем сообщение об ограничении
                            Toast.makeText(ChoosingPlace.this, "Можно выбрать не более 3 мест", Toast.LENGTH_SHORT).show();
                        }
                        selectedSeatsCount = selectedSeats.size();

// Обновляем счетчик выбранных билетов и общую сумму
                        int totalPrice = selectedSeatsCount * screening.getPrice();


                        TextView ticketsCountView = findViewById(R.id.tickets_count);
                        ticketsCountView.setText(getString(R.string.tickets_count, selectedSeatsCount));

                        TextView totalPriceView = findViewById(R.id.total_price);
                        totalPriceView.setText(getString(R.string.total_price, totalPrice));
                        if (selectedSeats.size() > 0) {
                            continueButton.setEnabled(true);
                        } else {
                            continueButton.setEnabled(false);
                        }

                    }
                });
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(10, 10, 10, 10); // отступы между местами
            params.columnSpec = GridLayout.spec(columnIndex, 1);
            params.rowSpec = GridLayout.spec(rowIndex, 1);

            seatTable.addView(seatView, params);

            columnIndex++;
            if (columnIndex > numColumns) {
                columnIndex = 1;
                rowIndex++;
            }
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

                if (!isLoggedIn) {
                    Toast.makeText(ChoosingPlace.this, "Необходимо авторизоваться", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, Authorization.class);
//            startActivity(intent);
//            finish();
                }
                else {
                    Intent intent = new Intent(ChoosingPlace.this, Bron.class);
                    intent.putExtra("selected_seats", selectedSeats);
                    intent.putExtra("screening", selectedScreening);
                    startActivity(intent);
                }


            }
        });

    }


}

