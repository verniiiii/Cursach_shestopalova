package com.example.cursach_shestopalova;

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

public class FragmentCinemas extends Fragment {

    private RecyclerView recyclerView;
    private CinemaAdapterProst cinemaAdapterProst;
    private DBHelper dbHelper;
    private List<Cinema> cinemas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinemas, container, false);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Initialize data list
        cinemas = new ArrayList<>();

        // Fetch cinemas from database
        cinemas = dbHelper.getAllCinemas();
        if (cinemas == null) {
            cinemas = new ArrayList<>();
        }

        // Initialize adapter
        cinemaAdapterProst = new CinemaAdapterProst(cinemas, getContext());

        // Find RecyclerView in layout
        recyclerView = view.findViewById(R.id.container_cinemas);

        // Set LayoutManager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Attach adapter to RecyclerView
        recyclerView.setAdapter(cinemaAdapterProst);

        // Log for debugging
        if (recyclerView.getAdapter() == null) {
            Log.e("FragmentCinemas", "Adapter is not attached to RecyclerView!");
        } else {
            Log.d("FragmentCinemas", "Adapter attached successfully.");
        }

        return view;
    }

    private void updateCinemas() {
        List<Cinema> newCinemas = dbHelper.getAllCinemas();
        if (newCinemas != null) {
            cinemaAdapterProst.setCinemas(newCinemas);
        }
    }
}
