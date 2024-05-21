package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfile extends Fragment {

    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private DBHelper dbHelper;
    private List<Ticket> tickets;
    private Button buttonButton2; // Добавляем ссылку на кнопку
    private Button buttonButton1; // Добавляем ссылку на кнопку
    private TextView textView1; // Добавляем ссылку на кликабельный текст
    private TextView textView2; // Добавляем ссылку на кликабельный текст



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует


        buttonButton2 = view.findViewById(R.id.buttonButton2); // Инициализируем кнопку
        buttonButton1 = view.findViewById(R.id.buttonButton); // Инициализируем кнопку

        if (userId != -1) {
            buttonButton2.setVisibility(View.VISIBLE);
            buttonButton1.setVisibility(View.GONE);
            DBHelper dbHelper = new DBHelper(getContext());
            List<String> t = dbHelper.findUserLoginAndNameById(userId);

            TextView name = view.findViewById(R.id.textViewName);
            TextView login = view.findViewById(R.id.textViewLogin);
            name.setText(t.get(1));
            login.setText(t.get(0));


        }
        else {

        }

        textView2 = view.findViewById(R.id.t2); // Добавляем инициализацию кликабельного текста

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CallMe.class);
                startActivity(intent);
            }
        });
        buttonButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        buttonButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Сбрасываем идентификатор пользователя в SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", -1);
                editor.apply();

                // Начинаем новую активность MainPage
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);


            }
        });


        return view;
    }
}
