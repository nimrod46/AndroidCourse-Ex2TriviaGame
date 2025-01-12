package com.example.ex2triviagame.data.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ex2triviagame.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionBank {
    public static List<Question> getQuestions(Context context) {
        List<Question> questions = new ArrayList<>(loadQuestionsFromDB(context));
        return questions;
    }

    @SuppressLint("range")
    public static List<Question> loadQuestionsFromDB(Context context) {
        List<Question> questionList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Questions", null);

        if (cursor.moveToFirst()) {
            do {
                String text = cursor.getString(cursor.getColumnIndex("question_text"));
                String option1 = cursor.getString(cursor.getColumnIndex("option_1"));
                String option2 = cursor.getString(cursor.getColumnIndex("option_2"));
                String option3 = cursor.getString(cursor.getColumnIndex("option_3"));
                String option4 = cursor.getString(cursor.getColumnIndex("option_4"));
                String correctAnswer = cursor.getString(cursor.getColumnIndex("correct_answer"));
                var options = new ArrayList<>(Arrays.asList(option1, option2, option3, option4));
                questionList.add(new MultipleChoiceQuestion(text,options, correctAnswer));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return questionList;
    }
}
