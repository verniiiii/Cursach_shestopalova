package com.example.cursach_shestopalova;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScreeningAdapter extends RecyclerView.Adapter<ScreeningAdapter.ScreeningViewHolder> {
    private List<Screening> screenings;
    private Context context;


    public ScreeningAdapter(List<Screening> screenings, Context context) {
        this.screenings = screenings;
        this.context = context;
    }

    @NonNull
    @Override
    public ScreeningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new ScreeningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreeningViewHolder holder, int position) {
        Screening screening = screenings.get(position);
        holder.session_time.setText(screening.getTime());
        holder.session_hall.setText(String.valueOf(screening.getHall_id()));
        holder.session_price.setText(String.valueOf(screening.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Screening selectedScreening = screenings.get(position);
                    // Здесь вы можете передать объект Screening во вторую активность
                    Intent intent = new Intent(context, ChoosingPlace.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedScreening", selectedScreening);
                    bundle.putSerializable("screenings", (ArrayList<Screening>) screenings); // добавьте эту строку
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return screenings.size();
    }

    public static class ScreeningViewHolder extends RecyclerView.ViewHolder {
        public TextView session_time;
        public TextView session_hall;
        public TextView session_price;

        public ScreeningViewHolder(@NonNull View itemView) {
            super(itemView);
            session_time = itemView.findViewById(R.id.session_time);
            session_hall = itemView.findViewById(R.id.session_hall);
            session_price = itemView.findViewById(R.id.session_price);
        }
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
        notifyDataSetChanged();
    }
}
