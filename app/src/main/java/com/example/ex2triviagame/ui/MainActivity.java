package com.example.ex2triviagame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex2triviagame.data.model.MultipleChoiceQuestion;
import com.example.ex2triviagame.data.model.Question;
import com.example.ex2triviagame.data.model.QuestionBank;
import com.example.ex2triviagame.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int userScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView questionTextView = findViewById(R.id.questionTextView);
        Button[] answerButtons = new Button[]{
                findViewById(R.id.option1Button),
                findViewById(R.id.option2Button),
                findViewById(R.id.option3Button),
                findViewById(R.id.option4Button)
        };

        questions = QuestionBank.getQuestions();
        loadQuestion(questionTextView, answerButtons);
    }

    private void loadQuestion(TextView questionTextView, Button[] answerButtons) {
        if (currentQuestionIndex >= questions.size()) {
            showResults();
            return;
        }

        Question question = questions.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestionText());

        if (question instanceof MultipleChoiceQuestion) {
            List<String> options = ((MultipleChoiceQuestion) question).getOptions();
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(options.get(i));
                int finalI = i;
                answerButtons[i].setOnClickListener(v -> {
                    if (question.checkAnswer(options.get(finalI))) {
                        userScore++;
                    }
                    currentQuestionIndex++;
                    loadQuestion(questionTextView, answerButtons);
                });
            }
        }
    }

    private void showResults() {
        // Navigate to the ResultActivity (same as before)
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("USER_SCORE", userScore);
        intent.putExtra("TOTAL_QUESTIONS", questions.size());
        startActivity(intent);
        finish();
    }
}