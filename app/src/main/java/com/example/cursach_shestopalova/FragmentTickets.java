package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Initialize data list
        tickets = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // возвращаем -1, если ключ отсутствует
        // Fetch cinemas from database
        // Fetch cinemas from database
        tickets = dbHelper.getAllTicketsById(userId);
        if (tickets == null) {
            tickets = new ArrayList<>();
        }

// Log ticket IDs
        for (Ticket ticket : tickets) {
            Log.d("FragmentTickets", "Ticket ID: " + ticket.getId());
        }


        // Initialize adapter
        ticketAdapter = new TicketAdapter(tickets, getContext());

        // Find RecyclerView in layout
        recyclerView = view.findViewById(R.id.container_cinemas);

        // Set LayoutManager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Attach adapter to RecyclerView
        recyclerView.setAdapter(ticketAdapter);

        // Log for debugging
        if (recyclerView.getAdapter() == null) {
            Log.e("FragmentCinemas", "Adapter is not attached to RecyclerView!");
        } else {
            Log.d("FragmentCinemas", "Adapter attached successfully.");
        }

        return view;
    }

//    private void updateTickets() {
//        List<Ticket> newTickets = dbHelper.getAllTicketsById();
//        if (newTickets != null) {
//            ticketAdapter.setTickets(newTickets);
//        }
//    }
}
