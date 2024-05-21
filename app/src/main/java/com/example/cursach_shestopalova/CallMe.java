package com.example.cursach_shestopalova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class CallMe extends AppCompatActivity {

    private FaqAdapter faqAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_me);

        DBHelper dbHelper = new DBHelper(this);
        List<Faq> faqs = dbHelper.getAllFaq();
        Log.d("CallMe", "Number of FAQs: " + faqs.size());

        // Initialize adapter
        faqAdapter = new FaqAdapter(faqs, this);

        // Find RecyclerView in layout
        recyclerView = findViewById(R.id.recyclerView);

        // Set LayoutManager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Attach adapter to RecyclerView
        recyclerView.setAdapter(faqAdapter);
    }
}