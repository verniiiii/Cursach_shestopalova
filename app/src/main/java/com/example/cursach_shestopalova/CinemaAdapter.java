package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {
    private List<Cinema> cinemas;
    private Context context;


    public CinemaAdapter(List<Cinema> cinemas, Context context) {
        this.cinemas = cinemas;
        this.context = context;
    }


    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cinema_for_activity_movie, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);
        Log.d("CinemaAdapter", "Cinema: " + cinema.getName() + ", Screenings: " + cinema.getScreenings().size());
        holder.cinema_name.setText(cinema.getName());
        holder.cinema_city.setText(cinema.getCity());
        holder.cinema_location.setText(cinema.getLocation());
        holder.cinema_description.setText(cinema.getDescription());

        // Создаем новый экземпляр адаптера для сеансов для каждого кинотеатра
        ScreeningAdapter screeningAdapter = new ScreeningAdapter(cinema.getScreenings(), context);
        holder.sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.sessionsRecyclerView.setAdapter(screeningAdapter);

        holder.cinema_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Cinema cinema = cinemas.get(position);
                    int cinemaId = cinema.getId();
                    // Здесь вы можете передать id фильма во вторую активность
                    Intent intent = new Intent(context, CinemaActivity.class);
                    intent.putExtra("cinema_id", cinemaId);
                    context.startActivity(intent);
                }
            }
        });

        holder.cinema_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Cinema cinema = cinemas.get(position);
                    int cinemaId = cinema.getId();
                    // Здесь вы можете передать id фильма во вторую активность
                    Intent intent = new Intent(context, CinemaActivity.class);
                    intent.putExtra("cinema_id", cinemaId);
                    context.startActivity(intent);
                }
            }
        });
        holder.cinema_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Cinema cinema = cinemas.get(position);
                    int cinemaId = cinema.getId();
                    // Здесь вы можете передать id фильма во вторую активность
                    Intent intent = new Intent(context, CinemaActivity.class);
                    intent.putExtra("cinema_id", cinemaId);
                    context.startActivity(intent);
                }
            }
        });
        holder.cinema_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Cinema cinema = cinemas.get(position);
                    int cinemaId = cinema.getId();
                    // Здесь вы можете передать id фильма во вторую активность
                    Intent intent = new Intent(context, CinemaActivity.class);
                    intent.putExtra("cinema_id", cinemaId);
                    context.startActivity(intent);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return cinemas.size();
    }

    public static class CinemaViewHolder extends RecyclerView.ViewHolder {
        public TextView cinema_name;
        public TextView cinema_city;
        public TextView cinema_location;
        public TextView cinema_description;
        public RecyclerView sessionsRecyclerView;

        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            cinema_name = itemView.findViewById(R.id.cinema_name);
            cinema_city = itemView.findViewById(R.id.cinema_city);
            cinema_location = itemView.findViewById(R.id.cinema_location);
            cinema_description = itemView.findViewById(R.id.cinema_description);
            sessionsRecyclerView = itemView.findViewById(R.id.sessions_recycler_view);
        }
    }

    public void setCinemas(List<Cinema> cinemas) {
        this.cinemas = cinemas;
        notifyDataSetChanged();
    }


}
