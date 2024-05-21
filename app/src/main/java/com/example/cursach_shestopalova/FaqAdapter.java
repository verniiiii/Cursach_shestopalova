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

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {
    private List<Faq> faqs;
    private Context context;

    public FaqAdapter(List<Faq> faqs, Context context) {
        this.faqs = faqs;
        this.context = context;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_questions, parent, false);
        return new FaqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder holder, int position) {
        Faq faq = faqs.get(position);



        holder.question.setText(faq.getQuestion());
        holder.answer.setText(faq.getAnswer());

    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    public static class FaqViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public TextView answer;

        public FaqViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);

        }
    }

    public void setTickets(List<Faq> faqs) {
        this.faqs = faqs;
        notifyDataSetChanged();
    }
}
