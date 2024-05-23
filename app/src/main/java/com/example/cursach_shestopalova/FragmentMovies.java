package com.example.cursach_shestopalova;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

public class FragmentMovies extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter1 movieAdapter1;
    private MovieAdapter2 movieAdapter2;

    private DBHelper dbHelper;
    private SwitchCompat mySwitch;
    private List<Movie> movieList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        dbHelper = new DBHelper(getContext());
        movieList = dbHelper.getAllMovies();

        recyclerView = view.findViewById(R.id.container_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        movieAdapter1 = new MovieAdapter1(movieList, getContext());
        recyclerView.setAdapter(movieAdapter1);

        EditText editText = view.findViewById(R.id.find);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String find_film = editText.getText().toString();
                List<Movie> filterMovies;
                filterMovies = filterMoviesBySubstring(movieList, find_film);

                if (mySwitch != null) {
                    if (mySwitch.isChecked()) {
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        movieAdapter2 = new MovieAdapter2(filterMovies, getContext());
                        recyclerView.setAdapter(movieAdapter2);
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        movieAdapter1 = new MovieAdapter1(filterMovies, getContext());
                        recyclerView.setAdapter(movieAdapter1);
                    }
                }
            }
        });
        dbHelper.close();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_1, menu);

        MenuItem menuItem = menu.findItem(R.id.action_switch);

        mySwitch = (SwitchCompat) menuItem.getActionView();

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                    movieAdapter2 = new MovieAdapter2(movieList, getContext());
                    recyclerView.setAdapter(movieAdapter2);
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    movieAdapter1 = new MovieAdapter1(movieList, getContext());
                    recyclerView.setAdapter(movieAdapter1);
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public List<Movie> filterMoviesBySubstring(List<Movie> movies, String substring) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(substring.toLowerCase())) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }
}
