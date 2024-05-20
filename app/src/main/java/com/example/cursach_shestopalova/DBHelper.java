package com.example.cursach_shestopalova;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cinema_tickets.db";
    private static final int DATABASE_VERSION = 53;
    private Context mContext; // Контекст приложения

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context; // Сохраняем контекст приложения
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableCreate.createTables(db);
        DataInitializer.initializeDataUsers(db, mContext);
        DataInitializer.initializeDataCinemas(db);
        DataInitializer.initializeDataHalls(db);
        DataInitializer.initializeDataRows(db);
        DataInitializer.initializeDataPlaces(db);
        DataInitializer.initializeDataMovies(db);
        DataInitializer.initializeDataScreenings(db);
        DataInitializer.initializeDataTickets(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных при изменении версии
        db.execSQL("DROP TABLE IF EXISTS users");//Ключевое слово DROP TABLE используется для удаления таблицы, а фраза IF EXISTS гарантирует, что запрос не вызовет ошибку, если таблица не существует.
        db.execSQL("DROP TABLE IF EXISTS cinemas");
        db.execSQL("DROP TABLE IF EXISTS halls");
        db.execSQL("DROP TABLE IF EXISTS rows");
        db.execSQL("DROP TABLE IF EXISTS places");
        db.execSQL("DROP TABLE IF EXISTS movies");
        db.execSQL("DROP TABLE IF EXISTS screenings");
        db.execSQL("DROP TABLE IF EXISTS tickets");
        onCreate(db);
    }
    public void addUser(String login, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        //хеширование пароля
        String hashedPassword = hashPassword(password);

        // Проверяем, существует ли пользователь с таким же логином
        if (!checkUserExists(login, db)) {
            ContentValues values = new ContentValues();
            values.put("login", login);
            values.put("username", username);
            values.put("password", hashedPassword);

            long newRowId = db.insert("users", null, values);
            db.close();

            if (newRowId != -1) {
                showToast("Вы удачно зарегистрировались!");
            } else {
                showToast("Не удалось зарегистрироваться!");
            }
        } else {
            showToast("Пользователь с таким логином уже существует!");
        }
    }

    // Метод для проверки существования пользователя
    public boolean checkUserExists(String login, SQLiteDatabase db) {
        String[] projection = {"login"};//список столбцов, которые мы хотим получить из таблицы
        String selection = "login=?"; //используется для фильтрации результатов запроса. Здесь мы определяем, что мы ищем строки, где значение столбца "username" соответствует переданному значению username
        String[] selectionArgs = {login}; //это значение будет вставлено вместо заполнителя ? в запросе, который мы определили в строке selection.

        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        boolean userExists = cursor.moveToFirst(); //есть ли хотя бы одна строка в результирующим наборе
        cursor.close();

        return userExists;
    }

    public boolean checkUserAndPassword(String login, String password, SQLiteDatabase db){
        String[] projection = {"password"};
        String selection = "login=?";
        String[] selectionArgs = {login};

        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        boolean userExists = false;

        if (cursor.moveToFirst()) {
            String hashedPassword = cursor.getString(0);
            userExists = checkPassword(password, hashedPassword);
        }

        cursor.close();

        return userExists;
    }
    public int getUserIdByLoginAndPassword(String login, String password, SQLiteDatabase db) {
        // Захешируем введенный пароль
        String hashedPassword = hashPassword(password);

        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE login = ? AND password = ?", new String[]{login, hashedPassword});
        Log.d("Authorization", "SQL query: " + cursor.toString());
        Log.d("Authorization", "Cursor count: " + cursor.getCount());

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return userId;
    }

    private boolean checkPassword(String password, String hashedPassword) {
        try {
            // Вычисление MD5 хеша введенного пароля
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(password.getBytes());
            String newHash = Base64.encodeToString(digest, Base64.DEFAULT);

            // Сравнение хешей
            return newHash.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected String hashPassword(String password) {
        try {
            // Вычисление MD5 хеша пароля
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(password.getBytes());
            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserRole(String login) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"role"};
        String selection = "login=?";
        String[] selectionArgs = {login};

        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        String userRole = null;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("role");
            if (columnIndex != -1) {
                userRole = cursor.getString(columnIndex);
            }
            cursor.close();
        }


        return userRole;
    }
    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
    public String getAllUsersAsString() { //вывод всех пользователей
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"id", "username", "password", "role"};
        Cursor cursor = db.query("users", projection, null, null, null, null, null);

        StringBuilder usersText = new StringBuilder ();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int usernameIndex = cursor.getColumnIndex("username");
                int passwordIndex = cursor.getColumnIndex("password");
                int roleIndex = cursor.getColumnIndex("role");

                if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && roleIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String username = cursor.getString(usernameIndex);
                    String password = cursor.getString(passwordIndex);
                    String role = cursor.getString(roleIndex);

                    // Формирование текста для каждого пользователя
                    String userText = "User: ID=" + id + ", Username=" + username + ", Password=" + password + ", Role=" + role + "\n";
                    usersText.append(userText);
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            usersText.append("No users found!");
        }


        return usersText.toString();
    }

    public List<Movie> getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"id", "title", "description", "genr", "city", "director", "actors", "duration", "image_id"};
        Cursor cursor = db.query("movies", projection, null, null, null, null, null);

        List<Movie> movieList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int titleIndex = cursor.getColumnIndex("title");
                int descriptionIndex = cursor.getColumnIndex("description");
                int genrIndex = cursor.getColumnIndex("genr");
                int cityIndex = cursor.getColumnIndex("city");
                int directorIndex = cursor.getColumnIndex("director");
                int actorsIndex = cursor.getColumnIndex("actors");
                int durationIndex = cursor.getColumnIndex("duration");
                int image_idIndex = cursor.getColumnIndex("image_id");

                if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && genrIndex != -1 && cityIndex != -1 && directorIndex != -1 && actorsIndex != -1 && durationIndex != -1 && image_idIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    String genr = cursor.getString(genrIndex);
                    String city = cursor.getString(cityIndex);
                    String director = cursor.getString(directorIndex);
                    String actors = cursor.getString(actorsIndex);
                    int duration = cursor.getInt(durationIndex);
                    int image_id = cursor.getInt(image_idIndex);

                    // Создание объекта Movie и добавление его в список
                    Movie movie = new Movie(id, title, description, genr, city, director, actors, duration, image_id);
                    movieList.add(movie);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return movieList;
    }
    public List<Cinema> getAllCinemas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"id", "name", "city", "location", "description"};
        Cursor cursor = db.query("cinemas", projection, null, null, null, null, null);

        List<Cinema> cinemasList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int cityIndex = cursor.getColumnIndex("city");
                int locationIndex = cursor.getColumnIndex("location");
                int descriptionIndex = cursor.getColumnIndex("description");

                if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 && locationIndex != -1 && cityIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String city = cursor.getString(cityIndex);
                    String location = cursor.getString(locationIndex);
                    String description = cursor.getString(descriptionIndex);

                    // Создание объекта Movie и добавление его в список
                    Cinema cinema = new Cinema(id, name, city, location, description);
                    cinemasList.add(cinema);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return cinemasList;
    }
    public List<Screening> getScreeningsByCinemaIdAndMovieIdAndDate(int cinemaId, int movieId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                "cinema_id",
                "movie_id",
                "hall_id",
                "price",
                "date",
                "time"
        };

        String selection = "cinema_id = ? AND movie_id = ? AND date = ?";
        String[] selectionArgs = {String.valueOf(cinemaId), String.valueOf(movieId), date};

        Cursor cursor = db.query("screenings", projection, selection, selectionArgs, null, null, null);

        List<Screening> screenings = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int cinema_id = cursor.getInt(cursor.getColumnIndexOrThrow("cinema_id"));
            int movie_id = cursor.getInt(cursor.getColumnIndexOrThrow("movie_id"));
            int hall_id = cursor.getInt(cursor.getColumnIndexOrThrow("hall_id"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
            String screeningDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));

            Screening screening = new Screening(id, cinema_id, movie_id, hall_id, price, screeningDate, time);
            screenings.add(screening);
        }

        cursor.close();

        return screenings;
    }


    public long addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", movie.getTitle());
        values.put("description", movie.getDescription());
        values.put("genr", movie.getGenr());
        values.put("city", movie.getCity());
        values.put("director", movie.getDirector());
        values.put("actors", movie.getActors());
        values.put("duration", movie.getDuration());
        values.put("image_id", movie.getImage_id());

        long newRowId = db.insert("movies", null, values);
        db.close();

        return newRowId;
    }
    public Movie findMovieById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"id", "title", "description", "genr", "city", "director", "actors", "duration", "image_id"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("movies", projection, selection, selectionArgs, null, null, null);
        Movie movie = null;

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            int genrIndex = cursor.getColumnIndex("genr");
            int cityIndex = cursor.getColumnIndex("city");
            int directorIndex = cursor.getColumnIndex("director");
            int actorsIndex = cursor.getColumnIndex("actors");
            int durationIndex = cursor.getColumnIndex("duration");
            int image_idIndex = cursor.getColumnIndex("image_id");

            if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && genrIndex != -1 && cityIndex != -1 && directorIndex != -1 && actorsIndex != -1 && durationIndex != -1 && image_idIndex != -1) {
                int movieId = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);
                String genr = cursor.getString(genrIndex);
                String city = cursor.getString(cityIndex);
                String director = cursor.getString(directorIndex);
                String actors = cursor.getString(actorsIndex);
                int duration = cursor.getInt(durationIndex);
                int image_id = cursor.getInt(image_idIndex);

                movie = new Movie(movieId, title, description, genr, city, director, actors, duration, image_id);
            }
            cursor.close();
        }

        return movie;
    }
    public int updateMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", movie.getTitle());
        values.put("description", movie.getDescription());
        values.put("genr", movie.getGenr());
        values.put("city", movie.getCity());
        values.put("director", movie.getDirector());
        values.put("actors", movie.getActors());
        values.put("duration", movie.getDuration());
        values.put("image_id", movie.getImage_id());

        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};

        int count = db.update("movies", values, selection, selectionArgs);
        db.close();

        return count;
    }
    public int deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = db.delete("movies", selection, selectionArgs);
        db.close();

        return count;
    }

    public List<Cinema> getCinemasByMovieId(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"id", "name", "city", "location", "description"};
        String selection = "id IN (SELECT DISTINCT cinema_id FROM screenings WHERE movie_id = ?)";
        String[] selectionArgs = {String.valueOf(movieId)};

        Cursor cursor = db.query("cinemas", projection, selection, selectionArgs, null, null, null);
        List<Cinema> cinemaList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int cityIndex = cursor.getColumnIndex("city");
                int locationIndex = cursor.getColumnIndex("location");
                int descriptionIndex = cursor.getColumnIndex("description");

                if (idIndex != -1 && nameIndex != -1 && cityIndex != -1 && locationIndex != -1 && descriptionIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String city = cursor.getString(cityIndex);
                    String location = cursor.getString(locationIndex);
                    String description = cursor.getString(descriptionIndex);

                    // Создание объекта Cinema и добавление его в список
                    Cinema cinema = new Cinema(id, name, city, location, description);
                    cinemaList.add(cinema);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return cinemaList;
    }
    public List<Movie> getMoviesByCinemaId(int cinemaId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"id", "title", "description", "genr", "director", "actors", "city", "duration", "image_id"};
        String selection = "id IN (SELECT DISTINCT movie_id FROM screenings WHERE cinema_id = ?)";
        String[] selectionArgs = {String.valueOf(cinemaId)};

        Cursor cursor = db.query("movies", projection, selection, selectionArgs, null, null, null);
        List<Movie> movieList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int titleIndex = cursor.getColumnIndex("title");
                int descriptionIndex = cursor.getColumnIndex("description");
                int genreIndex = cursor.getColumnIndex("genr");
                int directorIndex = cursor.getColumnIndex("director");
                int actorsIndex = cursor.getColumnIndex("actors");
                int cityIndex = cursor.getColumnIndex("city");
                int durationIndex = cursor.getColumnIndex("duration");
                int imageIdIndex = cursor.getColumnIndex("image_id");

                if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && genreIndex != -1 && directorIndex != -1 && actorsIndex != -1 && cityIndex != -1 && durationIndex != -1 && imageIdIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    String genre = cursor.getString(genreIndex);
                    String director = cursor.getString(directorIndex);
                    String actors = cursor.getString(actorsIndex);
                    String city = cursor.getString(cityIndex);
                    int duration = cursor.getInt(durationIndex);
                    int imageId = cursor.getInt(imageIdIndex);

                    // Создание объекта Movie и добавление его в список
                    Movie movie = new Movie(id, title, description, genre, director, actors, city, duration, imageId);
                    movieList.add(movie);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return movieList;
    }





    public List<Screening> getScreeningsByMovieIdAndDate(int movieId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "screenings.id",
                "screenings.cinema_id",
                "screenings.movie_id",
                "screenings.hall_id",
                "screenings.price",
                "screenings.date",
                "screenings.time"
        };

        String selection = "screenings.movie_id = ? AND screenings.date = ?";
        String[] selectionArgs = {String.valueOf(movieId), date};

        Cursor cursor = db.query("screenings", projection, selection, selectionArgs, null, null, null);

        List<Screening> screenings = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("screenings.id"));
                int cinema_id = cursor.getInt(cursor.getColumnIndexOrThrow("screenings.cinema_id"));
                int movie_id = cursor.getInt(cursor.getColumnIndexOrThrow("screenings.movie_id"));
                int hall_id = cursor.getInt(cursor.getColumnIndexOrThrow("screenings.hall_id"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("screenings.price"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("screenings.time"));

                Screening screening = new Screening(id, cinema_id, movie_id, hall_id, price, date, time);
                screenings.add(screening);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return screenings;
    }
    public Cinema findCinemaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"id", "name", "city", "location", "description"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("cinemas", projection, selection, selectionArgs, null, null, null);
        Cinema cinema = null;

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int cityIndex = cursor.getColumnIndex("city");
            int locationIndex = cursor.getColumnIndex("location");
            int descriptionIndex = cursor.getColumnIndex("description");

            if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 && locationIndex != -1 && cityIndex != -1) {
                int cinemaId = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descriptionIndex);
                String location = cursor.getString(locationIndex);
                String city = cursor.getString(cityIndex);

                cinema = new Cinema(cinemaId, name, city, location, description);
            }
            cursor.close();
        }

        return cinema;
    }
    public String findNameCinemaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"name"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("cinemas", projection, selection, selectionArgs, null, null, null);
        Cinema cinema = null;
        String name="";

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");


            if (nameIndex != -1 ) {
                name = cursor.getString(nameIndex);
            }
            cursor.close();
        }

        return name;
    }
    public String findNameMovieById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"title"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("movies", projection, selection, selectionArgs, null, null, null);
        String title="";

        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");


            if (titleIndex != -1 ) {
                title = cursor.getString(titleIndex);
            }
            cursor.close();
        }

        return title;
    }
    public Integer findNomerHollById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"hall_number"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("halls", projection, selection, selectionArgs, null, null, null);
        int hall_number=-1;

        if (cursor != null && cursor.moveToFirst()) {
            int hall_numberIndex = cursor.getColumnIndex("hall_number");


            if (hall_numberIndex != -1 ) {
                hall_number = cursor.getInt(hall_numberIndex);
            }
            cursor.close();
        }

        return hall_number;
    }

    public Pair<Integer, Integer> findSeatAndRowIdById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"place_number", "row_id"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("places", projection, selection, selectionArgs, null, null, null);
        int seat_number = -1;
        int row_id = -1;

        if (cursor != null && cursor.moveToFirst()) {
            int seat_numberIndex = cursor.getColumnIndex("place_number");
            int row_idIndex = cursor.getColumnIndex("row_id");

            if (seat_numberIndex != -1) {
                seat_number = cursor.getInt(seat_numberIndex);
            }
            if (row_idIndex != -1) {
                row_id = cursor.getInt(row_idIndex);
            }
            cursor.close();
        }

        return new Pair<>(seat_number, row_id);
    }

    public Integer findRowById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"row_number"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("rows", projection, selection, selectionArgs, null, null, null);
        int hall_number=-1;

        if (cursor != null && cursor.moveToFirst()) {
            int hall_numberIndex = cursor.getColumnIndex("row_number");


            if (hall_numberIndex != -1 ) {
                hall_number = cursor.getInt(hall_numberIndex);
            }
            cursor.close();
        }

        return hall_number;
    }

    public Integer findHallById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"hall_number"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query("halls", projection, selection, selectionArgs, null, null, null);
        int hall_number=-1;

        if (cursor != null && cursor.moveToFirst()) {
            int hall_numberIndex = cursor.getColumnIndex("hall_number");


            if (hall_numberIndex != -1 ) {
                hall_number = cursor.getInt(hall_numberIndex);
            }
            cursor.close();
        }

        return hall_number;
    }
    public void addTickets(int screening_id, int place_id, int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("screening_id", screening_id);
        values.put("place_id", place_id);
        values.put("user_id", user_id);


        db.insert("tickets", null, values);
        db.close();
    }

//    public void deleteAllUsers() { //Удалить всех пользователей из бд
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete("users", null, null);
//        db.close();
//    }

//    public void resetIdCounter() { //для сброса id
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM sqlite_sequence WHERE name='users'");
//        db.close();
//        showToast("ID counter reset successfully!");
//    }

}
