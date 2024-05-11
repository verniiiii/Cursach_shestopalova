package com.example.cursach_shestopalova;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataInitializer {
    public static void initializeDataUsers(SQLiteDatabase db, Context context) {
        ContentValues adminValues = new ContentValues();
        adminValues.put("login", "admin@gmail.com");
        adminValues.put("username", "admin");
        adminValues.put("password", "admin_password");
        adminValues.put("role", "admin");
        db.insert("users", null, adminValues);

        DBHelper dbHelper = new DBHelper(context); // создаем экземпляр класса DBHelper

        ContentValues user1Values = new ContentValues();
        user1Values.put("login", "vera@gmail.com");
        user1Values.put("username", "Veronika");
        user1Values.put("password", dbHelper.hashPassword("40027061")); // хешируем пароль перед сохранением
        user1Values.put("role", "user");
        db.insert("users", null, user1Values);


        ContentValues user2Values = new ContentValues();
        user2Values.put("login", "dima@gmail.com");
        user2Values.put("username", "Dmitriy");
        user2Values.put("password", "12345678");
        user2Values.put("role", "user");
        db.insert("users", null, user2Values);

    }

    public static void initializeDataCinemas(SQLiteDatabase db) {
        ContentValues cinemaValues = new ContentValues();

        // Добавление первого кинотеатра
        cinemaValues.put("name", "Киномакс");
        cinemaValues.put("city", "Москва");
        cinemaValues.put("location", "ул. Пушкина, 12");
        cinemaValues.put("description", "Большой кинотеатр с современным оборудованием и широким выбором фильмов.");
        db.insert("cinemas", null, cinemaValues);

        // Добавление второго кинотеатра
        cinemaValues.put("name", "Киномир");
        cinemaValues.put("city", "Санкт-Петербург");
        cinemaValues.put("location", "Невский проспект, 50");
        cinemaValues.put("description", "Кинотеатр с комфортными креслами и широким выбором фильмов.");
        db.insert("cinemas", null, cinemaValues);

        // Добавление третьего кинотеатра
        cinemaValues.put("name", "Формула Кино");
        cinemaValues.put("city", "Казань");
        cinemaValues.put("location", "ул. Баумана, 70");
        cinemaValues.put("description", "Кинотеатр с премиальными залами и широким выбором фильмов.");
        db.insert("cinemas", null, cinemaValues);


    }
    public static void initializeDataHalls(SQLiteDatabase db) {
        ContentValues hallValues = new ContentValues();

        // Добавление залов для первого кинотеатра
        for (int i = 1; i <= 5; i++) {
            hallValues.put("cinema_id", 1);
            hallValues.put("hall_number", i);
            hallValues.put("capacity", 100);
            hallValues.put("role", "standard");
            db.insert("halls", null, hallValues);
        }

        // Добавление залов для второго кинотеатра
        for (int i = 1; i <= 7; i++) {
            hallValues.put("cinema_id", 2);
            hallValues.put("hall_number", i);
            hallValues.put("capacity", 150);
            if (i == 1 || i == 2) {
                hallValues.put("role", "vip");
            } else {
                hallValues.put("role", "standard");
            }
            db.insert("halls", null, hallValues);
        }

        // Добавление залов для третьего кинотеатра
        for (int i = 1; i <= 10; i++) {
            hallValues.put("cinema_id", 3);
            hallValues.put("hall_number", i);
            hallValues.put("capacity", 200);
            if (i == 1 || i == 2 || i == 3) {
                hallValues.put("role", "premium");
            } else if (i == 4 || i == 5) {
                hallValues.put("role", "vip");
            } else {
                hallValues.put("role", "standard");
            }
            db.insert("halls", null, hallValues);
        }


    }
    public static void initializeDataRows(SQLiteDatabase db) {
        ContentValues rowValues = new ContentValues();

        // Добавление рядов для первого кинотеатра
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 10; j++) {
                rowValues.put("hall_id", i);
                rowValues.put("capacity", 10);
                rowValues.put("row_number", j);
                db.insert("rows", null, rowValues);
            }
        }

        // Добавление рядов для второго кинотеатра
        for (int i = 6; i <= 12; i++) {
            for (int j = 1; j <= 15; j++) {
                rowValues.put("hall_id", i);
                rowValues.put("capacity", 10);
                rowValues.put("row_number", j);
                db.insert("rows", null, rowValues);
            }
        }

        // Добавление рядов для третьего кинотеатра
        for (int i = 13; i <= 22; i++) {
            for (int j = 1; j <= 20; j++) {
                rowValues.put("hall_id", i);
                rowValues.put("capacity", 10);
                rowValues.put("row_number", j);
                db.insert("rows", null, rowValues);
            }
        }


    }
    public static void initializeDataPlaces(SQLiteDatabase db) {
        ContentValues placeValues = new ContentValues();

        // Добавление мест для первого кинотеатра
        for (int i = 1; i <= 50; i++) {
            for (int j = 1; j <= 10; j++) {
                placeValues.put("row_id", i);
                placeValues.put("place_number", j);
                db.insert("places", null, placeValues);
            }
        }

        // Добавление мест для второго кинотеатра
        for (int i = 51; i <= 155; i++) {
            for (int j = 1; j <= 10; j++) {
                placeValues.put("row_id", i);
                placeValues.put("place_number", j);
                db.insert("places", null, placeValues);
            }
        }

        // Добавление мест для третьего кинотеатра
        for (int i = 156; i <= 355; i++) {
            for (int j = 1; j <= 10; j++) {
                placeValues.put("row_id", i);
                placeValues.put("place_number", j);
                db.insert("places", null, placeValues);
            }
        }
    }
    public static void initializeDataMovies(SQLiteDatabase db) {
        ContentValues movieValues = new ContentValues();

        // Добавление первого фильма
        movieValues.put("title", "Артур, ты король");
        movieValues.put("description", "Это было последнее соревнование капитана по приключенческим гонкам - Майкла Линднорда, он был полон решимости ничему не позволить встать у него на пути. Собрав первоклассную команду он не мог и представить, что в 700 километровом забеге у них появится неожиданный попутчик - пес по кличке Артур, встреча с которым изменит не только исход гонки, но и их жизнь\n");
        movieValues.put("genr", "приключения");
        movieValues.put("city", "США");
        movieValues.put("director", "Саймон Селлан Джоунс");
        movieValues.put("actors", "Марк Уолберг, Натали Эммануэль, Симу Лю , Майкл Лэндис, Пол Гилфойл");
        movieValues.put("duration", 113);
        movieValues.put("image_id", R.drawable.artur_you_korol);
        db.insert("movies", null, movieValues);

        // Добавление второго фильма
        movieValues.put("title", "Домовой");
        movieValues.put("description", "Осиротев, Варя и Арсений перебираются из родной деревни в дом к приемным родителям. Но надежда на счастливую жизнь угасает, когда им бросает вызов древняя потусторонняя сила.\n");
        movieValues.put("genr", "фолк-хоррор");
        movieValues.put("city", "США");
        movieValues.put("director", "Андрей Загидуллин");
        movieValues.put("actors", "Россия");
        movieValues.put("duration", 98);
        movieValues.put("image_id", R.drawable.domovoy);
        db.insert("movies", null, movieValues);

        // Добавление третьего фильма
        movieValues.put("title", "Кунг-фу Панда 4");
        movieValues.put("description", "Продолжение приключений легендарного Воина Дракона, его верных друзей и наставника.\n");
        movieValues.put("genr", "мультфильм, приключения, семейный");
        movieValues.put("city", "Китай, США");
        movieValues.put("director", "Стефани Стайн, Майк Митчелл");
        movieValues.put("actors", "Брайан Крэнстон, Дастин Хоффман, Джек Блэк, Нора Лам (Аквафина), Виола Дэвис");
        movieValues.put("duration", 105);
        movieValues.put("image_id", R.drawable.kung_fu);
        db.insert("movies", null, movieValues);

        // Добавление четвертого фильма
        movieValues.put("title", "Лёд 3");
        movieValues.put("description", "Надя выросла и стала фигуристкой. Она мечтает о «Кубке Льда», как когда-то мечтала ее мама. Горин возражает против спортивной карьеры дочери — он оберегает ее от любых трудностей и его можно понять: он потерял слишком много. На тайной тренировке Надя знакомится с молодым и дерзким хоккеистом из Москвы, и между ними вспыхивает первая любовь. Отец не верит в искренность чувств юноши и разлучает пару...\n");
        movieValues.put("genr", "мелодрама");
        movieValues.put("city", "Россия");
        movieValues.put("director", "Юрий Хмельницкий");
        movieValues.put("actors", "Александр Петров, Мария Аронова, Анна Савранская, Степан Белозеров, Сергей Лавыгин, Елена Николаева");
        movieValues.put("duration", 146);
        movieValues.put("image_id", R.drawable.led3);
        db.insert("movies", null, movieValues);

        // Добавление шестого фильма
        movieValues.put("title", "Сказки Гофмана");
        movieValues.put("description", "Живет в Москве неприметная девушка Надежда Страхова (Екатерина Вилкова). Ее муж Виталик (Максим Стоянов) женился на Надежде ради столичной прописки и типовой «хрущёвки» на окраине. Чтобы содержать семью, Надежда работает на двух работах: по утрам в районной библиотеке и вечерами в театральном гардеробе. Однажды её жизнь резко меняется. Хозяин очередного пальто Гарик (Евгений Цыганов) обращает внимание на красивые руки Надежды и неожиданно открывает перед ней сказочный мир кино и рекламы.\n");
        movieValues.put("genr", "драма, комедия");
        movieValues.put("city", "Россия");
        movieValues.put("director", "Тина Баркалая");
        movieValues.put("actors", "Екатерина Вилкова, Евгений Цыганов, Максим Стоянов, Алексей Гуськов, Ксения Кутепова, Наргис Абдуллаева");
        movieValues.put("duration", 103);
        movieValues.put("image_id", R.drawable.tale_gofmana);
        db.insert("movies", null, movieValues);

        // Добавление пятого фильма
        movieValues.put("title", "Ненормальный");
        movieValues.put("description", "8-летний Коля не такой как все. Мама мальчика Татьяна уже не верит, что врожденная болезнь отступит, и ребенок когда-нибудь станет «нормальным». Но ее новый знакомый Юрий твердо намерен поставить Колю на ноги с помощью своей уникальной системы гимнастики. А когда во время занятий случайно выясняется, что у мальчика талант к музыке, обучение игре на фортепиано становится еще одним этапом на пути к выздоровлению.\n" +
                "\n" +
                "Впереди у Коли — престижный конкурс пианистов в Китае, первое свидание и масса препятствий, преодолеть которые ему помогут целительная сила музыки и отцовской любви.");
        movieValues.put("genr", "драма, комедия");
        movieValues.put("city", "Россия");
        movieValues.put("director", "Илья Маланин");
        movieValues.put("actors", "Александр Яценко, Наталья Кудряшова, Елисей Свеженцев, Илларион Маров, Сюй Шиюэ, Надежда Маркина");
        movieValues.put("duration", 109);
        movieValues.put("image_id", R.drawable.nenormal);
        db.insert("movies", null, movieValues);


    }

    public static void initializeDataScreenings(SQLiteDatabase db) {
        ContentValues screeningValues = new ContentValues();

        // Добавление сеансов для первого кинотеатра
        for (int i = 1; i <= 5; i++) {
            screeningValues.put("cinema_id", 1);
            screeningValues.put("movie_id", 1);
            screeningValues.put("hall_id", i);
            screeningValues.put("date", "2024-05-11");
            screeningValues.put("time", "12:00");
            screeningValues.put("price", "500");
            db.insert("screenings", null, screeningValues);

            screeningValues.put("movie_id", 2);
            screeningValues.put("time", "15:00");
            screeningValues.put("price", "300");
            db.insert("screenings", null, screeningValues);

            screeningValues.put("movie_id", 3);
            screeningValues.put("time", "18:00");
            screeningValues.put("price", "350");
            db.insert("screenings", null, screeningValues);
        }

        // Добавление сеансов для второго кинотеатра
        for (int i = 6; i <= 12; i++) {
            screeningValues.put("cinema_id", 2);
            screeningValues.put("movie_id", 2);
            screeningValues.put("hall_id", i);
            screeningValues.put("date", "2024-05-12");
            screeningValues.put("time", "11:00");
            screeningValues.put("price", "500");
            db.insert("screenings", null, screeningValues);

            screeningValues.put("movie_id", 3);
            screeningValues.put("time", "14:00");
            screeningValues.put("price", "500");
            db.insert("screenings", null, screeningValues);

            screeningValues.put("movie_id", 4);
            screeningValues.put("time", "17:00");
            screeningValues.put("price", "200");
            db.insert("screenings", null, screeningValues);
        }

        // Добавление сеансов для третьего кинотеатра
        for (int i = 13; i <= 22; i++) {
            screeningValues.put("cinema_id", 3);
            screeningValues.put("movie_id", 3);
            screeningValues.put("hall_id", i);
            screeningValues.put("date", "2024-05-13");
            screeningValues.put("time", "10:00");
            screeningValues.put("price", "300");
            db.insert("screenings", null, screeningValues);

            screeningValues.put("movie_id", 5);
            screeningValues.put("time", "13:00");
            screeningValues.put("price", "250");
            db.insert("screenings", null, screeningValues);

            screeningValues.put("movie_id", 6);
            screeningValues.put("time", "16:00");
            screeningValues.put("price", "300");
            db.insert("screenings", null, screeningValues);
        }

        // Добавление первого фильма во все три кинотеатра в один день
        for (int i = 1; i <= 3; i++) {
            screeningValues.put("cinema_id", i);
            screeningValues.put("movie_id", 1);
            screeningValues.put("hall_id", 1);
            screeningValues.put("date", "2024-05-14");
            screeningValues.put("time", "10:00");
            screeningValues.put("price", "500");
            db.insert("screenings", null, screeningValues);
        }

        // Добавление второго фильма во все три кинотеатра в один день
        for (int i = 1; i <= 3; i++) {
            screeningValues.put("cinema_id", i);
            screeningValues.put("movie_id", 2);
            screeningValues.put("hall_id", 2);
            screeningValues.put("date", "2024-05-15");
            screeningValues.put("time", "15:00");
            screeningValues.put("price", "300");
            db.insert("screenings", null, screeningValues);
        }
    }



    public static void initializeDataTickets(SQLiteDatabase db) {
        // Добавление билетов для пользователя с id 2
        ContentValues ticketValues1 = new ContentValues();
        ticketValues1.put("screening_id", 1);
        ticketValues1.put("place_id", 1);
        ticketValues1.put("user_id", 2);
        db.insert("tickets", null, ticketValues1);

        ContentValues ticketValues2 = new ContentValues();
        ticketValues2.put("screening_id", 2);
        ticketValues2.put("place_id", 2);
        ticketValues2.put("user_id", 2);
        db.insert("tickets", null, ticketValues2);

        // Добавление билета для пользователя с id 3
        ContentValues ticketValues3 = new ContentValues();
        ticketValues3.put("screening_id", 3);
        ticketValues3.put("place_id", 3);
        ticketValues3.put("user_id", 3);
        db.insert("tickets", null, ticketValues3);
    }
}

