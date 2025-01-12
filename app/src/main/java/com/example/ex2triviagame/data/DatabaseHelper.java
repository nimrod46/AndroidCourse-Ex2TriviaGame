// DatabaseHelper.java
package com.example.ex2triviagame.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TriviaGame.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_QUESTIONS = "Questions";
    public static final String TABLE_SCORES = "Scores";

    // Users table columns
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Questions table columns
    public static final String COLUMN_QUESTION_ID = "id";
    public static final String COLUMN_QUESTION_TEXT = "question_text";
    public static final String COLUMN_OPTION_1 = "option_1";
    public static final String COLUMN_OPTION_2 = "option_2";
    public static final String COLUMN_OPTION_3 = "option_3";
    public static final String COLUMN_OPTION_4 = "option_4";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    // Scores table columns
    public static final String COLUMN_SCORE_ID = "id";
    public static final String COLUMN_USER_SCORE = "score";
    public static final String COLUMN_SCORE_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        addAllQuestions();
//        addUser("test", "123456");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Users table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL);");

        // Create Questions table
        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + " ("
                + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUESTION_TEXT + " TEXT NOT NULL, "
                + COLUMN_OPTION_1 + " TEXT NOT NULL, "
                + COLUMN_OPTION_2 + " TEXT NOT NULL, "
                + COLUMN_OPTION_3 + " TEXT NOT NULL, "
                + COLUMN_OPTION_4 + " TEXT NOT NULL, "
                + COLUMN_CORRECT_ANSWER + " TEXT NOT NULL);");

        // Create Scores table
        db.execSQL("CREATE TABLE " + TABLE_SCORES + " ("
                + COLUMN_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_USER_SCORE + " INTEGER NOT NULL, "
                + COLUMN_SCORE_DATE + " TEXT NOT NULL);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    @SuppressLint("range")
    public String validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? COLLATE NOCASE AND password = ?", new String[]{username, password});

        String realUsername = "";

        if (cursor.moveToFirst()) {
            realUsername = cursor.getString(cursor.getColumnIndex("username"));
        }

        cursor.close();
        db.close();
        return realUsername;
    }

    // Insert new user
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean addScore(String username, int score, String date) {
        SQLiteDatabase db = null;
        boolean success = false;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_USER_SCORE, score);
            values.put(COLUMN_SCORE_DATE, date);

            long result = db.insert(TABLE_SCORES, null, values);
            success = (result != -1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return success;
    }

    public void addAllQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, null, null);

        // Example questions
        addQuestion("What is the capital of France?", "Paris", "Berlin", "Madrid", "Rome", "Paris");
        addQuestion("Which planet is known as the Red Planet?", "Earth", "Mars", "Jupiter", "Venus", "Mars");
        addQuestion("What is the largest ocean on Earth?", "Atlantic", "Pacific", "Indian", "Arctic", "Pacific");
        addQuestion("Who wrote 'Hamlet'?", "Charles Dickens", "William Shakespeare", "Mark Twain", "J.K. Rowling", "William Shakespeare");
        addQuestion("What is the boiling point of water in Celsius?", "90°C", "100°C", "110°C", "120°C", "100°C");

        db.close();
    }

    public boolean addQuestion(String questionText, String option1, String option2, String option3, String option4, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_TEXT, questionText);
        values.put(COLUMN_OPTION_1, option1);
        values.put(COLUMN_OPTION_2, option2);
        values.put(COLUMN_OPTION_3, option3);
        values.put(COLUMN_OPTION_4, option4);
        values.put(COLUMN_CORRECT_ANSWER, correctAnswer);

        long result = db.insert(TABLE_QUESTIONS, null, values);
        db.close();
        return result != -1; // Return true if insertion was successful
    }

    @SuppressLint("range")
    public String getLastScore(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String result = "No previous scores"; // Default message if no score is found

        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_USER_SCORE + ", " + COLUMN_SCORE_DATE +
                        " FROM " + TABLE_SCORES +
                        " WHERE " + COLUMN_USERNAME + " = ? ORDER BY " + COLUMN_SCORE_DATE + " DESC LIMIT 1",
                new String[]{username}
        );

        if (cursor.moveToFirst()) {
            int lastScore = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_SCORE));
            String scoreDate = cursor.getString(cursor.getColumnIndex(COLUMN_SCORE_DATE));
            result = "Last Score: " + lastScore + " on " + scoreDate;
        }

        cursor.close();
        db.close();
        return result; // Returns the formatted score and date, or a default message
    }
}
