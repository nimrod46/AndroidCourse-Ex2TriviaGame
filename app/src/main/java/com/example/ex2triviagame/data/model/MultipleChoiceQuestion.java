package com.example.ex2triviagame.data.model;

import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private final List<String> options;
    private final String correctAnswer;

    public MultipleChoiceQuestion(String questionText, List<String> options, String correctAnswer) {
        super(questionText);
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        return correctAnswer.equalsIgnoreCase(userAnswer);
    }
}
