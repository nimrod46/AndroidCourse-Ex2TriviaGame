package com.example.ex2triviagame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex2triviagame.R;
import com.example.ex2triviagame.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("USER_SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        saveScore(score);
        TextView resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setText("You scored " + score + " out of " + totalQuestions);

        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveScore(int score) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String username = getSharedPreferences("TriviaGamePrefs", MODE_PRIVATE)
                .getString("username", "Unknown");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        dbHelper.addScore(username, score, currentDate);
    }
}
