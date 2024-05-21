package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CinemaAdapterProst extends RecyclerView.Adapter<CinemaAdapterProst.CinemaViewHolder> {
    private List<Cinema> cinemas;
    private Context context;

    public CinemaAdapterProst(List<Cinema> cinemas, Context context) {
        this.cinemas = cinemas;
        this.context = context;
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cinema_for_activity_movie2, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);
        Log.d("CinemaAdapter", "Cinema: " + cinema.getName());
        holder.cinema_name.setText(cinema.getName());
        holder.cinema_city.setText(cinema.getCity());
        holder.cinema_location.setText(cinema.getLocation());
        holder.cinema_description.setText(cinema.getDescription());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Cinema selectedCinema = cinemas.get(pos);
                    Intent intent = new Intent(context, CinemaActivity.class);
                    intent.putExtra("cinema_id", selectedCinema.getId());
                    context.startActivity(intent);
                }
            }
        };

        holder.cinema_name.setOnClickListener(onClickListener);
        holder.cinema_city.setOnClickListener(onClickListener);
        holder.cinema_location.setOnClickListener(onClickListener);
        holder.cinema_description.setOnClickListener(onClickListener);
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

        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            cinema_name = itemView.findViewById(R.id.cinema_name);
            cinema_city = itemView.findViewById(R.id.cinema_city);
            cinema_location = itemView.findViewById(R.id.cinema_location);
            cinema_description = itemView.findViewById(R.id.cinema_description);
        }
    }

    public void setCinemas(List<Cinema> newCinemas) {
        this.cinemas = newCinemas;
        notifyDataSetChanged();
    }
}
