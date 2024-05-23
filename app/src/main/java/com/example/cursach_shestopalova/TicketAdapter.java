package com.example.cursach_shestopalova;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> tickets;
    private Context context;

    public TicketAdapter(List<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        int screening_id = ticket.getScreening_id();
        int place_id = ticket.getPlace_id();
        int user_id = ticket.getUser_id();
        DBHelper dbHelper = new DBHelper(context);

        Pair<Integer, Integer> seatAndRow = dbHelper.findSeatAndRowIdById(place_id);
        int seat_number = seatAndRow.first;
        int row_id = seatAndRow.second;

        holder.place.setText(String.valueOf(seat_number));

        int row_number = dbHelper.findRowById(row_id);
        holder.row.setText(String.valueOf(row_number));

        Screening screening = dbHelper.findScreeningById(screening_id);

        holder.movie.setText(dbHelper.findNameMovieById(screening.getMovie_id()));
        holder.cinema.setText(dbHelper.findNameCinemaById(screening.getCinema_id()));
        holder.hall.setText(String.valueOf(dbHelper.findNomerHollById(screening.getHall_id())));

        holder.data.setText(screening.getDate());
        holder.time.setText(screening.getTime());
        holder.price.setText(String.valueOf(screening.getPrice()));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        public TextView movie;
        public TextView cinema;
        public TextView hall;
        public TextView row;
        public TextView place;
        public TextView data;
        public TextView time;
        public TextView price;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            movie = itemView.findViewById(R.id.movie);
            cinema = itemView.findViewById(R.id.cinema);
            hall = itemView.findViewById(R.id.hall);
            row = itemView.findViewById(R.id.row);
            place = itemView.findViewById(R.id.place);
            data = itemView.findViewById(R.id.data);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);
        }
    }

    public void setTickets(List<Ticket> newTickets) {
        this.tickets = newTickets;
        notifyDataSetChanged();
    }
}
