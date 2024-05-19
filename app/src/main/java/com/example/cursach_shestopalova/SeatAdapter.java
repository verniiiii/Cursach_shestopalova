package com.example.cursach_shestopalova;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

public class SeatAdapter extends BaseAdapter {
    private ArrayList<Place> places;
    private Context context;

    public SeatAdapter(ArrayList<Place> places, Context context) {
        this.places = places;
        this.context = context;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Создаем кнопку для места
        Button seatButton = new Button(context);
        seatButton.setText(String.valueOf(places.get(position).getNumber()));
        seatButton.setBackgroundResource(R.drawable.kung_fu);
        seatButton.setLayoutParams(new GridLayout.LayoutParams(
                GridLayout.spec(places.get(position).getRowNumber(), 1),
                GridLayout.spec(places.get(position).getNumber(), 1)
        ));

        // Добавляем кнопку в GridLayout
        return seatButton;
    }
}

