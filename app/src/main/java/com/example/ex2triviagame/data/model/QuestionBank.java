package com.example.ex2triviagame.data.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new MultipleChoiceQuestion("What is the capital of France?",
                List.of("Paris", "London", "Berlin", "Madrid"), "Paris"));
        questions.add(new MultipleChoiceQuestion("Who wrote 'Hamlet'?",
                List.of("Shakespeare", "Tolstoy", "Hemingway", "Orwell"), "Shakespeare"));
        questions.add(new MultipleChoiceQuestion("What is the largest planet in our Solar System?",
                List.of("Mars", "Earth", "Jupiter", "Saturn"), "Jupiter"));
        return questions;
    }
}
