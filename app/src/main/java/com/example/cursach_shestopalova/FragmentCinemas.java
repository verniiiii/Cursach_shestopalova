package com.example.cursach_shestopalova;

import android.os.Bundle;
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

        dbHelper = new DBHelper(getContext());
        cinemas = new ArrayList<>();

        cinemas = dbHelper.getAllCinemas();
        if (cinemas == null) {
            cinemas = new ArrayList<>();
        }

        cinemaAdapterProst = new CinemaAdapterProst(cinemas, getContext());
        recyclerView = view.findViewById(R.id.container_cinemas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cinemaAdapterProst);

        return view;
    }
}
