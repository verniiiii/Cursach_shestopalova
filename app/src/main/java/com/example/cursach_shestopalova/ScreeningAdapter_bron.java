package com.example.cursach_shestopalova;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScreeningAdapter_bron extends RecyclerView.Adapter<ScreeningAdapter_bron.ScreeningViewHolder> {

    private List<Screening> screenings;
    private Context context;
    private Screening selectedScreening;
    private int selectedScreeningPosition;
    private ChoosingPlace choosingPlace; // добавляем поле для ссылки на активность


    public ScreeningAdapter_bron(List<Screening> screenings, Context context, Screening sel, ChoosingPlace choosingPlace) {
        this.screenings = screenings;
        this.context = context;
        this.selectedScreening = sel;
        this.choosingPlace = choosingPlace; // сохраняем ссылку на активность
    }

    @NonNull
    @Override
    public ScreeningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        ScreeningViewHolder viewHolder = new ScreeningViewHolder(view);

        // Выбираем первый элемент по умолчанию
        if (selectedScreening == null && screenings.size() > 0) {
            selectedScreening = screenings.get(0);
            selectedScreeningPosition = 0;
            viewHolder.itemView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.active_icon_color));
            // Устанавливаем цвет текста для выбранного элемента
        }

        // Вызываем метод updateHallView для выбранного элемента
        if (selectedScreening != null) {
            choosingPlace.updateHallView(selectedScreening);
        }

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ScreeningViewHolder holder, int position) {
        Screening screening = screenings.get(position);
        holder.session_time.setText(screening.getTime());
        holder.session_hall.setText(String.valueOf(screening.getHall_id()));
        holder.session_price.setText(String.valueOf(screening.getPrice()));

        // Обновляем отображение элемента в зависимости от того, выбран он или нет
        if (screening.equals(selectedScreening)) {
            // Выбранный элемент
            holder.itemView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.active_icon_color));
            holder.session_time.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.session_hall.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.session_price.setTextColor(ContextCompat.getColor(context, R.color.black));

        } else {
            // Невыбранный элемент
            holder.itemView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.inactive_icon_color));
            holder.session_time.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.session_hall.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.session_price.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Screening newSelectedScreening = screenings.get(position);
                    int old = selectedScreeningPosition;
                    selectedScreening = newSelectedScreening;
                    selectedScreeningPosition = position; // Обновляем позицию выбранного элемента
                    notifyItemChanged(old);
                    notifyItemChanged(selectedScreeningPosition);
                    choosingPlace.updateHallView(selectedScreening); // передаем параметры в метод


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
    public Screening getSelectedScreening() {
        return selectedScreening;
    }
}
