package com.lyl.test9.ui.home;

import android.os.Bundle;
import android.widget.TextView;

import com.lyl.test9.R;
import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news);
        TextView title = findViewById(R.id.title);
        TextView passage = findViewById(R.id.passage);
        title.setText("title");
        passage.setText("passage");
    }
}
