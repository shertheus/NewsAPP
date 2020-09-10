package com.lyl.news.ui.home.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lyl.news.R;

public class NewsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news);
        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        TextView URL = findViewById(R.id.URL);
        TextView author = findViewById(R.id.author);
        TextView passage = findViewById(R.id.passage);
        title.setText("title");
        passage.setText("passage");
    }
}
