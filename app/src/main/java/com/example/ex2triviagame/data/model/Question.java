package com.example.ex2triviagame.data.model;

public abstract class Question {
    private final String questionText;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public abstract boolean checkAnswer(String userAnswer);
}
