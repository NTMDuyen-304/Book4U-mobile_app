package com.example.book4u.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book4u.R;
import com.google.android.material.appbar.MaterialToolbar;

public class BookDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbarBookDetail;
    private TextView tvBookDetailTitle;
    private TextView tvBookDetailAuthor;
    private TextView tvBookDetailStatus;
    private TextView tvBookDetailIntro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        toolbarBookDetail = findViewById(R.id.toolbarBookDetail);
        tvBookDetailTitle = findViewById(R.id.tvBookDetailTitle);
        tvBookDetailAuthor = findViewById(R.id.tvBookDetailAuthor);
        tvBookDetailStatus = findViewById(R.id.tvBookDetailStatus);
        tvBookDetailIntro = findViewById(R.id.tvBookDetailIntro);

        toolbarBookDetail.setNavigationOnClickListener(v -> finish());

        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String intro = getIntent().getStringExtra("intro");
        boolean available = getIntent().getBooleanExtra("available", true);

        if (title == null || title.isEmpty()) title = "Unknown Book";
        if (author == null || author.isEmpty()) author = "Unknown Author";
        if (intro == null || intro.isEmpty()) intro = "No introduction available for this book yet.";

        tvBookDetailTitle.setText(title);
        tvBookDetailAuthor.setText(author);
        tvBookDetailIntro.setText(intro);

        if (available) {
            tvBookDetailStatus.setText("Available");
            tvBookDetailStatus.setTextColor(Color.parseColor("#6F42C1"));
        } else {
            tvBookDetailStatus.setText("Out of stock");
            tvBookDetailStatus.setTextColor(Color.parseColor("#D97706"));
        }
    }
}