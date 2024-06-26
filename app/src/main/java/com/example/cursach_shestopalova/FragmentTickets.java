package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentTickets extends Fragment {

    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private DBHelper dbHelper;
    private List<Ticket> tickets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        dbHelper = new DBHelper(getContext());

        tickets = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует

        tickets = dbHelper.getAllTicketsById(userId);
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        ImageView imageView = view.findViewById(R.id.image_cinema);
        LinearLayout linearLayout = view.findViewById(R.id.linear);

        Button button = view.findViewById(R.id.button_select_session);
        if (tickets.size()!=0){
            imageView = view.findViewById(R.id.image_cinema);
            linearLayout = view.findViewById(R.id.linear);
            imageView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);

            ticketAdapter = new TicketAdapter(tickets, getContext());
            recyclerView = view.findViewById(R.id.container_cinemas);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(ticketAdapter);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainPage.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
