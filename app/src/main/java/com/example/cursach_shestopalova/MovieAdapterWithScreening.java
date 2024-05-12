package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieAdapterWithScreening extends RecyclerView.Adapter<MovieAdapterWithScreening.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;

    public MovieAdapterWithScreening(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_with_screening, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.imageView.setImageResource(movie.getImage_id());
        holder.titleTextView.setText(movie.getTitle());
        holder.genreTextView.setText(movie.getGenr());
        holder.cityTextView.setText(movie.getCity());

        // Создаем новый экземпляр адаптера для сеансов для каждого фильма
        ScreeningAdapter screeningAdapter = new ScreeningAdapter(movie.getScreenings(), context);
        holder.sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.sessionsRecyclerView.setAdapter(screeningAdapter);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Movie movie = movies.get(position);
                    int movieId = movie.getId();
                    // Здесь вы можете передать id фильма во вторую активность
                    // будет страница самого фильма(актёры ии тд)
                    Intent intent = new Intent(context, MovieActivity.class);
                    intent.putExtra("movie_id", movieId);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleTextView;
        public TextView genreTextView;
        public TextView cityTextView;
        public RecyclerView sessionsRecyclerView; // Добавляем RecyclerView для сеансов

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            sessionsRecyclerView = itemView.findViewById(R.id.sessions_recycler_view); // Инициализируем RecyclerView для сеансов
        }
    }
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}

