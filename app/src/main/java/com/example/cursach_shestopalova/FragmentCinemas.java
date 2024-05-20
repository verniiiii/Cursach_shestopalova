package com.example.cursach_shestopalova;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FragmentCinemas extends Fragment {

    private RecyclerView recyclerView;
    private CinemaAdapterProst cinemaAdapterProst;

    private DBHelper dbHelper;
    private SwitchCompat mySwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cinemas, container, false);

        DBHelper dbHelper1 = new DBHelper(getActivity());

        List<Cinema> cinemas = dbHelper1.getAllCinemas();

        if (cinemas != null && cinemas.size() > 0) {
            recyclerView = view.findViewById(R.id.container_cinemas);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

            cinemaAdapterProst = new CinemaAdapterProst(cinemas, getContext());
            recyclerView.setAdapter(cinemaAdapterProst);
        } else {
            // здесь можно вывести сообщение об отсутствии данных
            Toast.makeText(getActivity(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }




        return view;
    }


}
